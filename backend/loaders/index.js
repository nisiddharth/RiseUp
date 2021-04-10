const databaseLoader = require('./databaseLoader');
const jobLoader = require('./jobLoader');
const firebaseLoader = require('./firebaseLoader');

module.exports = async () => {
    databaseLoader();
    jobLoader();
    firebaseLoader();
}
