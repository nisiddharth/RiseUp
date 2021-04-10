const { Router } = require('express');
const route = Router();

const { saveDeviceToken, saveEmotion, getEmotions, addFriend, getFriends, acceptInvite, getAllRequests, removeDeviceToken } = require('../controllers/user');
const { saveDeviceTokenValidator, saveEmotionValidator } = require('../validators/user');

route.post('/save/token', saveDeviceTokenValidator, saveDeviceToken);
route.delete('/remove/token', removeDeviceToken);

route.post('/save/emotion', saveEmotionValidator, saveEmotion);
route.post('/add/friend', addFriend);
route.post('/accept/invite', acceptInvite);
route.get('/requests', getAllRequests);

route.get('/get/friends', getFriends);
route.get('/get/emotion', getEmotions);

module.exports = route;