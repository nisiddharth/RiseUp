exports.appConfig = {
    port: process.env.PORT || 5000,
    jwtSecret: process.env.JWT_SECRET,
}

exports.databaseConfig = {
    connectionString: process.env.DATABASE_CONNECTION_STRING,
}