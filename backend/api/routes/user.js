const { Router } = require('express');
const route = Router();

const { saveDeviceToken } = require('../controllers/user');
const { saveDeviceTokenValidator } = require('../validators/user');

route.post('/save/token', saveDeviceTokenValidator, saveDeviceToken);

module.exports = route;