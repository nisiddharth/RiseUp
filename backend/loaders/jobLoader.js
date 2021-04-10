const scheduler = require('node-schedule');
const { startMonitor } = require('../jobs/emotionMonitor')

module.exports = () => {

    scheduler.scheduleJob('/2 * * * *', startMonitor);
}