const { Router } = require('express');
const route = Router();

const { saveDeviceToken, saveEmotion, getEmotions, addFriend, getFriends } = require('../controllers/user');
const { saveDeviceTokenValidator, saveEmotionValidator } = require('../validators/user');

route.post('/save/token', saveDeviceTokenValidator, saveDeviceToken);
route.post('/save/emotion', saveEmotionValidator, saveEmotion);
route.post('/add/friend', addFriend);

route.get('/get/friends', getFriends);
route.get('/get/emotion', getEmotions);

module.exports = route;