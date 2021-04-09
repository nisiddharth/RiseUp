const { Router } = require('express');
const route = Router();

const authRoute = require('./auth');


route.get('/', (req, res) => {
    return res.json({
        "Message": "Server is up and running!",
    })
});

route.use('/auth', authRoute);

module.exports = route;


