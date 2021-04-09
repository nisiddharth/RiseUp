const { check, validationResult } = require('express-validator');

exports.saveDeviceTokenValidator = [
    check('token').exists(),
    (req, res, next) => {
        const errors = validationResult(req);
        if (!errors.isEmpty()) {
            return res.status(422).json({
                status: 0,
                error: errors.array(),
            });
        }
        next();
    }
]