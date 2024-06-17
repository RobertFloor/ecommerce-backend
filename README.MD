```bash
docker run -e "ACCEPT_EULA=Y" -e "MSSQL_SA_PASSWORD=Password123*" \                       43m 33s
   -p 1433:1433 --name sql1 --hostname sql1 \
   mcr.microsoft.com/mssql/server:2022-latest
docker exec -it sql1 "bash"
mssql@sql1:/$ /opt/mssql-tools/bin/sqlcmd -S localhost -U SA -P 'Password123*'

```

```sql
1> CREATE LOGIN TEST_USER WITH PASSWORD = 'YourStrong!Passw0rd'
2> GO
1> QUIT
mssql@sql1:/$ /opt/mssql-tools/bin/sqlcmd -S localhost -U SA -P 'Password123*'
1> CREATE DATABASE ecommerce
2> GO
```