const { Router } = require('express');
const route = Router();

const { signIn, signUp } = require('../controllers/auth');
const { signInValidator, signUpValidator } = require('../validators/auth');

route.post('/signIn', signInValidator, signIn);
route.post('/signUp', signUpValidator, signUp);

module.exports = route;