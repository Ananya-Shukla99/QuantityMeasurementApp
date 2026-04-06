#!/bin/bash

# ═══════════════════════════════════════════════════════════════════════════════
# Quantity Measurement App - Quick Start Script
# ═══════════════════════════════════════════════════════════════════════════════

set -e

# Colors for output
RED='\033[0;31m'
GREEN='\033[0;32m'
YELLOW='\033[1;33m'
BLUE='\033[0;34m'
NC='\033[0m' # No Color

echo -e "${BLUE}╔════════════════════════════════════════════════════════════════╗${NC}"
echo -e "${BLUE}║     Quantity Measurement App - Quick Start                     ║${NC}"
echo -e "${BLUE}╚════════════════════════════════════════════════════════════════╝${NC}"
echo ""

# Check prerequisites
echo -e "${YELLOW}[1/5] Checking prerequisites...${NC}"
echo ""

# Check Java
if ! command -v java &> /dev/null; then
    echo -e "${RED}✗ Java not found. Please install Java 17+${NC}"
    exit 1
fi
JAVA_VERSION=$(java -version 2>&1 | awk -F '"' '/version/ {print $2}' | cut -d. -f1)
if [ "$JAVA_VERSION" -lt 17 ]; then
    echo -e "${RED}✗ Java version $JAVA_VERSION found, need 17+${NC}"
    exit 1
fi
echo -e "${GREEN}✓ Java ${JAVA_VERSION} found${NC}"

# Check Maven
if ! command -v mvn &> /dev/null; then
    echo -e "${RED}✗ Maven not found. Please install Maven 3.9+${NC}"
    exit 1
fi
echo -e "${GREEN}✓ Maven found: $(mvn -version | head -1)${NC}"

# Check Docker
if ! command -v docker &> /dev/null; then
    echo -e "${RED}✗ Docker not found. Please install Docker${NC}"
    exit 1
fi
echo -e "${GREEN}✓ Docker found: $(docker --version)${NC}"

# Check Docker Compose
if ! command -v docker-compose &> /dev/null; then
    echo -e "${RED}✗ Docker Compose not found. Please install Docker Compose${NC}"
    exit 1
fi
echo -e "${GREEN}✓ Docker Compose found: $(docker-compose --version)${NC}"

echo ""
echo -e "${YELLOW}[2/5] Building project with Maven...${NC}"
echo ""

# Build project
mvn clean install -DskipTests 2>&1 | grep -E "BUILD SUCCESS|BUILD FAILURE|ERROR" || true

if [ ${PIPESTATUS[0]} -ne 0 ]; then
    echo -e "${RED}✗ Maven build failed${NC}"
    exit 1
fi
echo -e "${GREEN}✓ Maven build successful${NC}"

echo ""
echo -e "${YELLOW}[3/5] Building Docker images...${NC}"
echo ""

# Build Docker images
docker-compose build --no-cache

echo -e "${GREEN}✓ Docker images built${NC}"

echo ""
echo -e "${YELLOW}[4/5] Starting services with Docker Compose...${NC}"
echo ""

# Start services
docker-compose up -d

echo -e "${GREEN}✓ Services started${NC}"

echo ""
echo -e "${YELLOW}[5/5] Waiting for services to be healthy...${NC}"
echo ""

# Wait for services to be ready
for i in {1..30}; do
    if curl -s http://localhost:8761/actuator/health > /dev/null 2>&1; then
        echo -e "${GREEN}✓ Eureka Server is ready${NC}"
        break
    fi
    if [ $i -eq 30 ]; then
        echo -e "${RED}✗ Eureka Server failed to start${NC}"
        exit 1
    fi
    echo "  Waiting for Eureka... ($i/30)"
    sleep 1
done

sleep 5

for i in {1..30}; do
    if curl -s http://localhost:8080/actuator/health > /dev/null 2>&1; then
        echo -e "${GREEN}✓ API Gateway is ready${NC}"
        break
    fi
    if [ $i -eq 30 ]; then
        echo -e "${RED}✗ API Gateway failed to start${NC}"
        exit 1
    fi
    echo "  Waiting for API Gateway... ($i/30)"
    sleep 1
done

echo ""
echo -e "${GREEN}╔════════════════════════════════════════════════════════════════╗${NC}"
echo -e "${GREEN}║              ✓ All Services Started Successfully!              ║${NC}"
echo -e "${GREEN}╚════════════════════════════════════════════════════════════════╝${NC}"
echo ""

echo -e "${BLUE}📊 Service URLs:${NC}"
echo -e "  Eureka Server:     ${YELLOW}http://localhost:8761${NC}"
echo -e "  API Gateway:       ${YELLOW}http://localhost:8080${NC}"
echo -e "  Swagger UI:        ${YELLOW}http://localhost:8080/swagger-ui.html${NC}"
echo -e "  Auth Service:      ${YELLOW}http://localhost:8081${NC}"
echo -e "  QMA Service:       ${YELLOW}http://localhost:8082${NC}"
echo ""

echo -e "${BLUE}📝 Useful Commands:${NC}"
echo -e "  View logs:         ${YELLOW}docker-compose logs -f${NC}"
echo -e "  Stop services:     ${YELLOW}docker-compose down${NC}"
echo -e "  Clean everything:  ${YELLOW}docker-compose down -v${NC}"
echo -e "  Restart gateway:   ${YELLOW}docker-compose restart api-gateway${NC}"
echo ""

echo -e "${BLUE}🔗 Frontend URL:${NC}"
echo -e "  ${YELLOW}https://quantity-measurement-app-frontend-olive.vercel.app/login${NC}"
echo ""

echo -e "${GREEN}✨ Ready to go! Visit http://localhost:8761 to see the Eureka dashboard.${NC}"
echo ""

