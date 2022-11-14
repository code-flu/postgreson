
.PHONY: package
package: ## Builds package
	mvn clean package \
	-Dfindbugs.skip=true -Dmaven.test.skip=true -Dcheckstyle.skip=true -Denforcer.skip=true

.PHONY: start_db
start_db: ## Start Postgres DB in docker container
	docker-compose -f docker-compose.yml up --remove-orphans

.PHONY: start_app
start_app:
	 set -o allexport && source local.env && \
	 java -jar target/*-jar-with-dependencies.jar server api/app.config.yml