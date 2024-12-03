import math
import torch
from torch import nn

x = torch.rand(128, 32, 512)
d_model = 512
n_head = 8

# Attention -- 动态聚合序列信息 -- 提高模型捕捉不同特征或关系的能力
class MutiHeadAttention(nn.Model):
  def _init_(self, d_model, n_head):
    super(MutiHeadAttention, self)._init_()
    self.n_head = n_head
    self.d_model = d_model
    self.w_q = nn.Linear(d_model, n_head)
    self.w_k = nn.Linear(d_model, n_head)
    self.w_v = nn.Linear(d_model, n_head)
    self.w_combine = nn.Linear(d_model, n_head)
    self.softmax = nn.Softmax(dim=-1)

  def forward(self, q, k, v, mask = None):
    batch, time, dimension = q.shape
    n_d = self.d_model // self.n_head
    q,k,v = self.w_q(q), self.w_k(k), self.w_v(v)
    q = q.view(batch, time, self.n_head, n_d).permute(0,2,1,3)
    k = k.view(batch, time, self.n_head, n_d).permute(0,2,1,3)
    v = v.view(batch, time, self.n_head, n_d).permute(0,2,1,3)
    score = q@k.transpose(2,3)/math.sqrt(n_d)
    if mask is not None:
      score = score.masked_fill(mask == 0, -10000)
    score = self.softmax(score)@v
    score = score.permute(0,2,1,3).contiguous().view(batch, time, dimension)
    out = self.w_combine(score)
    return out
  
  attention = MutiHeadAttention(d_model, n_head)
  out = attention(x,x,x)
  print(out)