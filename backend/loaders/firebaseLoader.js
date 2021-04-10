const admin = require('firebase-admin');
const serviceAccount = require('../serviceAccountKey.json');

module.exports = async () => {
    admin.initializeApp({
        credential: admin.credential.cert(serviceAccount),
    })
}