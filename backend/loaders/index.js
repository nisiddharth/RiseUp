const databaseLoader = require('./databaseLoader');
const jobLoader = require('./jobLoader');

module.exports = async () => {
    databaseLoader();
    jobLoader();
}
