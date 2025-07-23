# Trail Race Application Makefile
# ================================
# This Makefile provides commands to manage the development environment,
# build applications, and run tests for the Trail Race microservices project.

# Variables
COMPOSE_FILE := docker-compose.yml
COMMAND_SERVICE_DIR := race-application-command-service
QUERY_SERVICE_DIR := race-application-query-service
CLIENT_DIR := race_application_client

# Colors for terminal output
RED := \033[0;31m
GREEN := \033[0;32m
YELLOW := \033[1;33m
BLUE := \033[0;34m
NC := \033[0m # No Color

.PHONY: help dev-start dev-stop dev-restart dev-logs build test clean install docker-build docker-up docker-down docker-restart

# Default target
help: ## Show this help message
	@echo "$(BLUE)Trail Race Application - Development Commands$(NC)"
	@echo "=============================================="
	@echo ""
	@echo "$(GREEN)Development Environment:$(NC)"
	@echo "  make dev-start     - Start complete development environment"
	@echo "  make dev-stop      - Stop all services"
	@echo "  make dev-restart   - Restart all services"
	@echo "  make dev-logs      - Show logs from all services"
	@echo "  make dev-status    - Show status of all containers"
	@echo ""
	@echo "$(GREEN)Build Commands:$(NC)"
	@echo "  make build         - Build all services (Java + Docker)"
	@echo "  make build-java    - Build only Java services"
	@echo "  make build-client  - Build only Angular client"
	@echo "  make docker-build  - Build Docker images"
	@echo ""
	@echo "$(GREEN)Test Commands:$(NC)"
	@echo "  make test          - Run all tests"
	@echo "  make test-command  - Run command service tests"
	@echo "  make test-query    - Run query service tests"
	@echo "  make test-client   - Run client tests"
	@echo ""
	@echo "$(GREEN)Database Commands:$(NC)"
	@echo "  make db-reset      - Reset database (drop volumes)"
	@echo "  make db-connect    - Connect to PostgreSQL database"
	@echo "  make db-seed       - Manually seed database with test data"
	@echo ""
	@echo "$(GREEN)Utility Commands:$(NC)"
	@echo "  make clean         - Clean build artifacts and containers"
	@echo "  make install       - Install all dependencies"
	@echo "  make rabbitmq-ui   - Open RabbitMQ management UI"
	@echo "  make health-check  - Check health of all services"

# =============================================================================
# DEVELOPMENT ENVIRONMENT
# =============================================================================

dev-start: ## Start complete development environment
	@echo "$(GREEN)🚀 Starting Trail Race development environment...$(NC)"
	@docker-compose up -d
	@echo "$(GREEN)✅ Development environment started!$(NC)"
	@echo "$(YELLOW)📋 Services available at:$(NC)"
	@echo "  🌐 Client:          http://localhost:4200"
	@echo "  ⚙️  Command Service: http://localhost:8080"
	@echo "  🔍 Query Service:   http://localhost:8081"
	@echo "  🐰 RabbitMQ UI:     http://localhost:15672 (user/password)"
	@echo "  🗄️  PostgreSQL:     localhost:5432 (postgres/postgres)"

dev-stop: ## Stop all services
	@echo "$(YELLOW)🛑 Stopping development environment...$(NC)"
	@docker-compose down
	@echo "$(GREEN)✅ All services stopped!$(NC)"

dev-restart: ## Restart all services
	@echo "$(YELLOW)🔄 Restarting development environment...$(NC)"
	@docker-compose restart
	@echo "$(GREEN)✅ All services restarted!$(NC)"

dev-logs: ## Show logs from all services
	@echo "$(BLUE)📋 Showing logs from all services...$(NC)"
	@docker-compose logs -f

dev-status: ## Show status of all containers
	@echo "$(BLUE)📊 Container status:$(NC)"
	@docker-compose ps

# =============================================================================
# BUILD COMMANDS
# =============================================================================

build: build-java build-client docker-build ## Build all services (Java + Docker)
	@echo "$(GREEN)🏗️  Complete build finished!$(NC)"

build-java: ## Build only Java services
	@echo "$(BLUE)☕ Building Java services...$(NC)"
	@echo "$(YELLOW)Building Command Service...$(NC)"
	@cd $(COMMAND_SERVICE_DIR) && ./mvnw clean package -DskipTests
	@echo "$(YELLOW)Building Query Service...$(NC)"
	@cd $(QUERY_SERVICE_DIR) && ./mvnw clean package -DskipTests
	@echo "$(GREEN)✅ Java services built successfully!$(NC)"

build-client: ## Build only Angular client
	@echo "$(BLUE)🅰️  Building Angular client...$(NC)"
	@cd $(CLIENT_DIR) && npm run build --prod
	@echo "$(GREEN)✅ Angular client built successfully!$(NC)"

docker-build: ## Build Docker images
	@echo "$(BLUE)🐳 Building Docker images...$(NC)"
	@docker-compose build --no-cache
	@echo "$(GREEN)✅ Docker images built successfully!$(NC)"

# =============================================================================
# TEST COMMANDS
# =============================================================================

test: test-command test-query test-client ## Run all tests
	@echo "$(GREEN)🧪 All tests completed!$(NC)"

test-command: ## Run command service tests
	@echo "$(BLUE)🧪 Running Command Service tests...$(NC)"
	@cd $(COMMAND_SERVICE_DIR) && ./mvnw test
	@echo "$(GREEN)✅ Command Service tests passed!$(NC)"

test-query: ## Run query service tests
	@echo "$(BLUE)🧪 Running Query Service tests...$(NC)"
	@cd $(QUERY_SERVICE_DIR) && ./mvnw test
	@echo "$(GREEN)✅ Query Service tests passed!$(NC)"

test-client: ## Run client tests
	@echo "$(BLUE)🧪 Running Angular client tests...$(NC)"
	@cd $(CLIENT_DIR) && npm test -- --watch=false --browsers=ChromeHeadless
	@echo "$(GREEN)✅ Client tests passed!$(NC)"

# =============================================================================
# DATABASE COMMANDS
# =============================================================================

db-reset: ## Reset database (drop volumes and restart)
	@echo "$(YELLOW)🗄️  Resetting database...$(NC)"
	@docker-compose down -v
	@docker-compose up -d db
	@echo "$(GREEN)✅ Database reset complete!$(NC)"

db-connect: ## Connect to PostgreSQL database
	@echo "$(BLUE)🗄️  Connecting to PostgreSQL...$(NC)"
	@docker exec -it $$(docker-compose ps -q db) psql -U postgres -d postgres

db-seed: ## Manually seed database with test data
	@echo "$(BLUE)🌱 Seeding database with test data...$(NC)"
	@docker exec -i $$(docker-compose ps -q db) psql -U postgres -d postgres < init.sql
	@echo "$(GREEN)✅ Database seeded successfully!$(NC)"

# =============================================================================
# UTILITY COMMANDS
# =============================================================================

install: ## Install all dependencies
	@echo "$(BLUE)📦 Installing dependencies...$(NC)"
	@echo "$(YELLOW)Installing Command Service dependencies...$(NC)"
	@cd $(COMMAND_SERVICE_DIR) && ./mvnw dependency:resolve
	@echo "$(YELLOW)Installing Query Service dependencies...$(NC)"
	@cd $(QUERY_SERVICE_DIR) && ./mvnw dependency:resolve
	@echo "$(YELLOW)Installing Angular client dependencies...$(NC)"
	@cd $(CLIENT_DIR) && npm install
	@echo "$(GREEN)✅ All dependencies installed!$(NC)"

clean: ## Clean build artifacts and containers
	@echo "$(YELLOW)🧹 Cleaning build artifacts...$(NC)"
	@cd $(COMMAND_SERVICE_DIR) && ./mvnw clean
	@cd $(QUERY_SERVICE_DIR) && ./mvnw clean
	@cd $(CLIENT_DIR) && rm -rf dist/ node_modules/.cache/
	@docker-compose down -v --rmi local --remove-orphans
	@docker system prune -f
	@echo "$(GREEN)✅ Cleanup complete!$(NC)"

rabbitmq-ui: ## Open RabbitMQ management UI
	@echo "$(BLUE)🐰 Opening RabbitMQ Management UI...$(NC)"
	@echo "$(YELLOW)URL: http://localhost:15672$(NC)"
	@echo "$(YELLOW)Credentials: user / password$(NC)"
	@open http://localhost:15672 2>/dev/null || xdg-open http://localhost:15672 2>/dev/null || echo "Please open http://localhost:15672 manually"

health-check: ## Check health of all services
	@echo "$(BLUE)🏥 Checking service health...$(NC)"
	@echo "$(YELLOW)Checking Command Service...$(NC)"
	@curl -f http://localhost:8080/actuator/health 2>/dev/null && echo "$(GREEN)✅ Command Service: Healthy$(NC)" || echo "$(RED)❌ Command Service: Unhealthy$(NC)"
	@echo "$(YELLOW)Checking Query Service...$(NC)"
	@curl -f http://localhost:8081/actuator/health 2>/dev/null && echo "$(GREEN)✅ Query Service: Healthy$(NC)" || echo "$(RED)❌ Query Service: Unhealthy$(NC)"
	@echo "$(YELLOW)Checking Client...$(NC)"
	@curl -f http://localhost:4200 2>/dev/null && echo "$(GREEN)✅ Client: Healthy$(NC)" || echo "$(RED)❌ Client: Unhealthy$(NC)"
	@echo "$(YELLOW)Checking Database...$(NC)"
	@docker exec $$(docker-compose ps -q db) pg_isready -U postgres 2>/dev/null && echo "$(GREEN)✅ Database: Healthy$(NC)" || echo "$(RED)❌ Database: Unhealthy$(NC)"
	@echo "$(YELLOW)Checking RabbitMQ...$(NC)"
	@curl -f http://localhost:15672 2>/dev/null && echo "$(GREEN)✅ RabbitMQ: Healthy$(NC)" || echo "$(RED)❌ RabbitMQ: Unhealthy$(NC)"

# =============================================================================
# DOCKER SHORTCUTS
# =============================================================================

docker-up: dev-start ## Alias for dev-start

docker-down: dev-stop ## Alias for dev-stop

docker-restart: dev-restart ## Alias for dev-restart

# =============================================================================
# CI/CD COMMANDS
# =============================================================================

ci-build: ## Build for CI/CD pipeline
	@echo "$(BLUE)🔧 Running CI/CD build...$(NC)"
	@make build-java
	@make test
	@make docker-build
	@echo "$(GREEN)✅ CI/CD build completed!$(NC)"

ci-test: ## Run tests for CI/CD pipeline
	@echo "$(BLUE)🧪 Running CI/CD tests...$(NC)"
	@make test
	@echo "$(GREEN)✅ CI/CD tests completed!$(NC)"

# =============================================================================
# DEVELOPMENT SHORTCUTS
# =============================================================================

dev: dev-start ## Quick alias for dev-start

logs: dev-logs ## Quick alias for dev-logs

status: dev-status ## Quick alias for dev-status

up: dev-start ## Quick alias for dev-start

down: dev-stop ## Quick alias for dev-stop

restart: dev-restart ## Quick alias for dev-restart