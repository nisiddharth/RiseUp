const { Router } = require('express');
const route = Router();


route.get('/', (req, res) => {
    return res.json({
        "Message": "Server is up and running!",
    })
});

module.exports = route;


