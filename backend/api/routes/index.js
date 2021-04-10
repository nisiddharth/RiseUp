const { Router } = require('express');
const route = Router();

const authRoute = require('./auth');
const userRoute = require('./user');
const videoRoute = require('./videos');

const { userAuthentication } = require('../middlewares/authentication');

route.get('/', (req, res) => {
    return res.json({
        "Message": "Server is up and running!",
    })
});

route.use('/auth', authRoute);
route.use('/user', userAuthentication, userRoute);
route.use('/video', videoRoute);

module.exports = route;


