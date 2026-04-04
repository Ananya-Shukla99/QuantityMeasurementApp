# Environment Configuration Setup Guide

## Overview
This guide explains how to set up environment variables for the Auth Service using the `.env` file. This approach keeps sensitive credentials out of version control.

---

## File Structure

```
auth-service/
├── .env              # Local environment variables (NOT committed to git)
├── .env.example      # Template showing required variables (committed to git)
├── .gitignore        # Git ignore rules
└── src/main/resources/
    └── application.properties  # Configuration that references .env variables
```

---

## Setup Instructions

### 1. Initial Setup for New Developers

1. Clone the repository
2. Navigate to the `auth-service` directory
3. Copy the `.env.example` file to create your local `.env`:
   ```bash
   cp .env.example .env
   ```
4. Edit the `.env` file with your actual credentials:
   ```bash
   # Update database credentials
   DATABASE_USERNAME=your_username
   DATABASE_PASSWORD=your_password
   
   # Update OAuth2 credentials
   GITHUB_CLIENT_ID=your_github_id
   GITHUB_CLIENT_SECRET=your_github_secret
   
   # etc...
   ```

### 2. Loading Environment Variables

#### Option A: Using IDE (Recommended for Development)
Most IDEs can load `.env` files automatically. For IntelliJ IDEA/Eclipse:
1. Install the `.env` file plugin
2. The variables will be automatically loaded

#### Option B: Using Maven
Add the `dotenv-maven-plugin` to your `pom.xml`:
```xml
<plugin>
    <groupId>me.paulschwarz</groupId>
    <artifactId>dotenv-maven-plugin</artifactId>
    <version>2.3.0</version>
    <executions>
        <execution>
            <goals>
                <goal>load</goal>
            </goals>
        </execution>
    </executions>
</plugin>
```

#### Option C: Using Command Line
Load environment variables before running the application:

**Windows (PowerShell):**
```powershell
# Load from .env file
$env:DATABASE_URL = Get-Content .env | Select-String '^DATABASE_URL=' | ForEach-Object { $_.Split('=')[1] }
mvn spring-boot:run
```

**Linux/Mac (Bash):**
```bash
export $(cat .env | grep -v '#' | xargs)
mvn spring-boot:run
```

#### Option D: Using Docker
If using Docker, pass environment variables:
```bash
docker run --env-file .env -p 8081:8081 auth-service:latest
```

---

## Environment Variables Reference

### Database Configuration
| Variable | Description | Example |
|----------|-------------|---------|
| `DATABASE_URL` | MySQL connection string | `jdbc:mysql://localhost:3306/...` |
| `DATABASE_USERNAME` | Database user | `root` |
| `DATABASE_PASSWORD` | Database password | `your_password` |
| `DATABASE_DRIVER` | JDBC driver class | `com.mysql.cj.jdbc.Driver` |

### JWT Configuration
| Variable | Description | Example |
|----------|-------------|---------|
| `JWT_SECRET` | Base64-encoded secret key | Base64 string (min 32 bytes) |
| `JWT_EXPIRATION` | Token expiration time (ms) | `86400000` (24 hours) |

### OAuth2 GitHub
| Variable | Description |
|----------|-------------|
| `GITHUB_CLIENT_ID` | GitHub OAuth app ID |
| `GITHUB_CLIENT_SECRET` | GitHub OAuth app secret |
| `GITHUB_SCOPE` | OAuth scopes required |
| `GITHUB_REDIRECT_URI` | Redirect URL after auth |

### OAuth2 Google
| Variable | Description |
|----------|-------------|
| `GOOGLE_CLIENT_ID` | Google OAuth app ID |
| `GOOGLE_CLIENT_SECRET` | Google OAuth app secret |
| `GOOGLE_SCOPE` | OAuth scopes required |
| `GOOGLE_REDIRECT_URI` | Redirect URL after auth |

### CORS Configuration
| Variable | Description | Example |
|----------|-------------|---------|
| `CORS_ALLOWED_ORIGINS` | Allowed frontend URLs | `http://localhost:4200` |
| `OAUTH2_REDIRECT_URI` | Frontend OAuth callback URL | `http://localhost:4200/oauth2/callback` |

### Service Configuration
| Variable | Description | Example |
|----------|-------------|---------|
| `EUREKA_SERVICE_URL` | Eureka discovery server | `http://localhost:8761/eureka/` |
| `SERVER_PORT` | Server port | `8081` |

---

## Security Best Practices

1. **Never Commit `.env`**: The `.env` file is in `.gitignore` and should never be committed
2. **Use `.env.example`**: Commit `.env.example` instead to show required variables
3. **Rotate Secrets**: Regularly update OAuth2 credentials and database passwords
4. **Environment-Specific Values**: Use different credentials for dev, staging, and production
5. **Secure Storage**: In production, use environment variables from secure vaults (AWS Secrets Manager, HashiCorp Vault, etc.)

---

## Troubleshooting

### Variables Not Loading
- Verify `.env` file is in the correct directory (`auth-service/`)
- Check file permissions: `chmod 644 .env`
- Ensure IDE has `.env` plugin installed
- Try restarting the IDE or Maven

### Database Connection Failed
- Verify MySQL is running
- Check `DATABASE_URL` format
- Verify `DATABASE_USERNAME` and `DATABASE_PASSWORD`
- Test connection: `mysql -h localhost -u root -p`

### OAuth2 Failures
- Verify Client ID and Client Secret are correct
- Check Redirect URI matches OAuth provider configuration
- Ensure frontend URL in `CORS_ALLOWED_ORIGINS` is correct
- Check OAuth provider credentials are active (not revoked)

---

## Production Deployment

For production environments:

1. **Do NOT use `.env` files** - Use environment variables from your hosting platform:
   - AWS Elastic Beanstalk: Environment variables
   - Heroku: Config vars
   - Kubernetes: Secrets
   - Docker: Compose environment files

2. **Example Docker Compose** (production):
   ```yaml
   version: '3.8'
   services:
     auth-service:
       image: auth-service:latest
       environment:
         DATABASE_URL: ${DATABASE_URL}
         DATABASE_USERNAME: ${DATABASE_USERNAME}
         DATABASE_PASSWORD: ${DATABASE_PASSWORD}
         GITHUB_CLIENT_ID: ${GITHUB_CLIENT_ID}
         # ... other variables
   ```

3. **Set production values** on your hosting platform:
   ```bash
   # Example with heroku
   heroku config:set DATABASE_URL=your_production_url
   heroku config:set GITHUB_CLIENT_ID=your_prod_id
   ```

---

## Additional Resources

- [Spring Boot Externalized Configuration](https://docs.spring.io/spring-boot/docs/current/reference/html/features.html#features.external-config)
- [GitHub OAuth Documentation](https://docs.github.com/en/developers/apps/building-oauth-apps)
- [Google OAuth 2.0 Setup](https://developers.google.com/identity/protocols/oauth2)
- [Twelve-Factor App - Config](https://12factor.net/config)

---

**Last Updated**: April 2026

