const mongoose = require('mongoose');

const EmotionSchema = new mongoose.Schema({
    data: [
        {
            type: Array,
        }
    ]
}, {
    timestamps: true,
});

module.exports = mongoose.model('emotions', EmotionSchema);