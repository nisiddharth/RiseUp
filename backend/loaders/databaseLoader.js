const mongoose = require('mongoose');
const { databaseConfig } = require('../config');

module.exports = async () => {
    //Connecting Mongoose-mongodb
    mongoose.connect(databaseConfig.connectionString, {
        useNewUrlParser: true,
        useUnifiedTopology: true,
        useCreateIndex: true,
    }).then(() => {
        console.log("MongoDB Connected...")
    }).catch((err) => {
        console.log("Problem Logging DB...");
        throw err;
    });

}