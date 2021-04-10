const mongoose = require('mongoose');
const crypto = require('crypto');
const uuidv1 = require('uuid').v1;

const UserSchema = new mongoose.Schema({
    name: {
        type: String,
        required: true,
    },
    email: {
        type: String,
        required: true,
        unique: true,
    },
    phone: {
        type: String,
        required: true,
        unique: true,
    },
    deviceTokens: [
        {
            type: String,
        }
    ],
    emotion: {
        type: mongoose.Types.ObjectId,
    },
    encryptedPassword: String,
    salt: String,
}, {
    timestamps: true,
    toObject: {
        virtuals: true,
    },
    toJSON: {
        virtuals: true,
    }
});


UserSchema.virtual('password').set(function (password) {
    this.salt = uuidv1();
    this.encryptedPassword = this.securePassword(password);
});

UserSchema.methods = {
    authenticate: function (plainPassword) {
        return this.securePassword(plainPassword) === this.encryptedPassword
    },

    securePassword: function (plainPassword) {
        if (!plainPassword) return "";
        try {
            return crypto.createHmac('sha256', this.salt)
                .update(plainPassword)
                .digest('hex');
        } catch (err) {
            return "";
        }
    }
}

module.exports = mongoose.model('user', UserSchema);