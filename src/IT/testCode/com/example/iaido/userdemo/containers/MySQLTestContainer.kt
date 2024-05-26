package shane.iaido.userdemo.containers

import org.testcontainers.containers.MySQLContainer

class MySQLTestContainer : MySQLContainer<MySQLTestContainer>() {

    companion object {
        private var container: MySQLTestContainer? = null

        fun getInstance(): MySQLTestContainer? {
            if (container == null) {
                container = MySQLTestContainer().withDatabaseName("public")
                        .withUsername("root")
                        .withPassword("password")
            }
            return container
        }
    }


}
