const { Router } = require('express');
const route = Router();

const authRoute = require('./auth');
const userRoute = require('./user');
const videoRoute = require('./videos');
const friendRoute = require('./friend');

const { userAuthentication } = require('../middlewares/authentication');

route.get('/', (req, res) => {
    return res.json({
        "Message": "Server is up and running!",
    })
});

route.use('/auth', authRoute);
route.use('/user', userAuthentication, userRoute);
route.use('/video', videoRoute);
route.use('/friend', friendRoute);

module.exports = route;


