import torch
from torch import nn
import torch.nn.functional as F
import math

random_torch = torch.rand(4,4)
print(random_torch)

#Embedding -- 将离散输入转为连续表示
#将输入词表索引转化为指定维度的Embedding
class TokenEmbedding(nn.Embedding):
  def __init__(self, vocab_size, d_model): #词汇表大小和embedding维度
    super(TokenEmbedding, self).__init__(vocab_size, d_model, padding_idx=1)

# 计算每个词的位置编码，使用cos和sin函数
class PositionalEmbedding(nn.Module):
  def __init__(self, d_model, max_len, device):
    super(PositionalEmbedding, self).__init__()
    self.encoding = torch.zeros(max_len, d_model, device=device)
    self.encoding.requires_grad = False
    pos = torch.arange(0, max_len, device=device)
    pos = pos.float().unsqueeze(dim=1) #转化为二维张量
    _2i = torch.arange(0, d_model, step=2, device=device).float()
    #定义奇偶
    self.encoding[:, 0::2] = torch.sin(pos/(10000**(_2i/d_model)))
    self.encoding[:, 1::2] = torch.cos(pos/(10000**(_2i/d_model)))
  
  def forward(self, x):
    batch_size, seq_len = x.size()
    return self.encoding[:seq_len, :] #返回编码矩阵中seq_len长度的内容
  
# 结合上述两部分，得到最后通过TansformerEmbedding处理的张量
class TransformerEmbedding(nn.Module):
  def __init__(self, vocab_size, d_model, max_len, drop_prob, device):
    super(TransformerEmbedding, self).__init__()
    self.tok_emb = TokenEmbedding(vocab_size, d_model)
    self.pos_emb = PositionalEmbedding(d_model, max_len, device)
    self.drop_out = nn.Dropout(p = drop_prob)
    
  def forward(self, x):
    tok_emb = self.tok_emb(x)
    pos_emb = self.pos_emb(x)
    return self.drop_out(tok_emb + pos_emb)
    