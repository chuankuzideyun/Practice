import torch
from torch import nn

# Attention -- 动态聚合序列信息 -- 提高模型捕捉不同特征或关系的能力
class MutiHeadAttention(nn.Model):
  def _init_(self, d_model, n_head):
    super(MutiHeadAttention, self)._init_()
    self.n_head = n_head
    self.d_model = d_model
    self.w_q = nn.Linear(d_model, n_head)
    self.w_k = nn.Linear(d_model, n_head)
    self.w_v = nn.Linear(d_model, n_head)