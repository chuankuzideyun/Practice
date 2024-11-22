import os
import sys
from torch.utils.data import DataLoader
from tqdm import tqdm #显示进度条

import torch
import torch.nn as nn
import torch.optim as optim

from data_loader import iris_dataload

#初始化神经网络模型
class NN(nn.Module):
  def __init__(self,in_dim, hidden_dim1,hidden_dim2, out_dim): #神经网络三个层级
    super().__init__()
    self.layer1 = nn.Linear(in_dim, hidden_dim1)
    self.layer2 = nn.Linear(hidden_dim1, hidden_dim2)
    self.layer3 = nn.Linear(hidden_dim2, out_dim)
  def forward(self,x): #对数据做怎样的处理
    x = self.layer1(x)
    x = self.layer2(x)
    x = self.layer3(x)
    return x

#定义计算环境
device = torch.device("cuda:0" if torch.cuda.is_available() else "cpu")
#训练集、验证集、测试集
custom_dataset = iris_dataload("./Iris_data.txt")
train_size = int(len(custom_dataset)*0.7)
val_size = int(len(custom_dataset)*0.2)
test_size = len(custom_dataset) - train_size - val_size

#对数据集按比例切分
train_dataset, val_dataset, test_dataset = torch.utils.data.random_split(custom_dataset, [train_size,val_size,test_size])
#按批量划分并加载数据集
train_loader = DataLoader(train_dataset, batch_size=16, shuffle=True) #每次抽取16个数据，下次抽取之前把所有数据打散重排
val_loader = DataLoader(val_dataset, batch_size=1, shuffle=False)
test_loader = DataLoader(test_dataset, batch_size=16, shuffle=True)
print("训练集大小", len(train_loader)*16, "验证集大小", len(train_loader), "测试集大小", len(train_loader))

#定义推理函数，计算并返回准确率
def infer(model, dataset, device):
  model.eval()
  acc_num = 0
  with torch.no_grad():
    for data in dataset:
      datas, label = data
      outputs = model(datas.to(device))
      #取出最大的ouputs作为预测结果
      predict_y = torch.max(outputs, dim=1)[1]
      acc_num += torch.eq(predict_y, label.to(device)).sum().item()
    acc = acc_num/len(dataset) #正确数量acc/数据总量
    return acc

def main(lr=0.005, epochs=20):
  model = NN(4,12,6,3).to(device) #中间是超参数，可以更改
  loss_f = nn.CrossEntropyLoss()
  pg = [p for p in model.parameters() if p.requires_grad] #三目运算语法
  optimizer = optim.Adam(pg, lr=lr) #优化器，对数据做更新
  #权重文件储存器
  save_path = os.path.join(os.getcwd(), "results/weight")
  if os.path.exists(save_path) is False:
    os.makedirs(save_path)
  
  #开始训练
  for epoch in range(epochs):
    model.train()
    acc_num = torch.zeros(1).to(device) #维度为1
    sample_num = 0
    
    train_bar = tqdm(train_loader, file=sys.stdout, ncols=100) #可展示进度条
    for datas in train_bar:
      data, label = datas
      label = label.squeeze(-1)
      #样本数累加
      sample_num += data.shape[0]

      optimizer.zero_grad() #优化器初始化，消除历史记录对当前的影响
      outputs = model(data.to(device))
      pred_class = torch.max(outputs, dim=1)[1] #torch.max返回元组，第一个元素是max的值，第二个是索引
      acc_num = torch.eq(pred_class, label.to(device)).sum()

      loss = loss_f(outputs, label.to(device))
      loss.backward()
      optimizer.step()

      #进度条
      train_acc = acc_num/sample_num
      train_bar.desc = "train epoch[{}/{}] loss:{:.3f}".format(epoch + 1, epochs, loss)
    val_acc = infer(model, val_loader, device)
    print("train epoch[{}/{}] loss:{: .3f} train_acc{:.3f} val_acc{:.3f}".format(epoch + 1, epochs, loss, train_acc, val_acc))
    torch.save(model.state_dict(), os.path.join(save_path, "nn.pth"))

    #每次数据集迭代之后，对初始化指标清零
    train_acc = 0
    val_acc = 0
  print("Finished Training")

  test_acc = infer(model, test_loader, device)
  print("test_acc", test_acc)

if __name__ == "__main__":
  main()