const { Router } = require('express');
const { getFriendEmotions } = require('../controllers/friend');
const route = Router();

route.get("/get/emotions", getFriendEmotions);

module.exports = route;