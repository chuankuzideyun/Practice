# 线性回归
import tensorflow as tf

def linear_regression():
  tf.compat.v1.disable_eager_execution()
  # 准备数据
  x = tf.random.normal(shape=[100,1])
  y_true = tf.matmul(x,[[0.8]])+0.7 # 一行一列
  # 构造函数
  weights = tf.Variable(initial_value= tf.random.normal(shape=[1,1]))
  bias = tf.Variable(initial_value= tf.random.normal(shape=[1,1]))
  y_predict = tf.matmul(x,weights) + bias
  # 损失函数
  error = tf.reduce_mean(tf.square(y_predict - y_true))
  # 优化损失
  optimizer = tf.compat.v1.train.GradientDescentOptimizer(learning_rate=0.01).minimize(error)
  # 显式初始化变量
  init = tf.compat.v1.global_variables_initializer()
  # 开启会话
  with tf.compat.v1.Session() as sess:
    # 初始化变量
    sess.run(init)
    # 查看初始化模型参数之后的值
    print("训练前的模型参数为：权重%f，偏置%f，损失为%f" % (weights.eval(), bias.eval(), error.eval()))
    #开始训练
    for i in range(1000):
      sess.run(optimizer)
      print("训练后的模型参数为：权重%f，偏置%f，损失为%f" % (weights.eval(), bias.eval(), error.eval()))
  return None

if __name__=="__main__":
  linear_regression()