const scheduler = require('node-schedule');
const { startMonitor } = require('../jobs/emotionMonitor')

module.exports = () => {
    startMonitor();
    scheduler.scheduleJob('/2 * * * *', startMonitor);
}