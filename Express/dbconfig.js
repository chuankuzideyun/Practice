const Sequelize = require("sequelize");

// 创建一个新的Sequelize实例
const DB = new Sequelize("auth", "root", "root", {
    host: "localhost", // 主机地址
    port: 3306, // 数据库端口号
    dialect: "mysql", // 数据库类型
    pool: {
        max: 5, // 最大连接数量
        min: 0, // 最小连接数量
        idle: 10000, // 如果10秒内没有被使用，释放该连接
    },
    logging: false, // 禁用Sequelize日志输出
});

// 测试数据库连接
DB
    .authenticate()
    .then(() => {
        console.log("数据库连接成功");
    })
    .catch((err) => {
        console.error("数据库连接失败:", err);
    });

module.exports = DB;
