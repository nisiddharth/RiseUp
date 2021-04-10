const { Router } = require('express');
const route = Router();

const { getVideos } = require('../controllers/videos');

route.get('/get', getVideos);

module.exports = route;