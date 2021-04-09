require('dotenv').config();

const express = require('express');
const cors = require('cors');
const { appConfig } = require('./config');
const loaders = require('./loaders');
const routes = require('./api/routes')

const startServer = async () => {
    const app = express();

    //Middle wares
    app.use(express.json());
    app.use(cors());

    //Adding routes
    app.use('/', routes);

    loaders();
    app.listen(appConfig.port, () => {
        console.log(`\nApp running on port ${appConfig.port}\n`);
    })
}

startServer();










