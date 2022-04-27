# Fullstack Redis project (Spring Java / Typescript)
### Backend: SpringBoot Redis 
#### REST API

| Endpoint<br/>URI | HTTP <br/>method | Post <br/>body | Result                               |
|------------------|------------------|----------------|--------------------------------------|
| /positions       | GET              | empty          | Find all positions                   |
| /positions/{id}  | GET              | empty          | Find position by <code>{id}</code>   |
| /positions       | POST             | JSON           | Create new position                  |
| /positions/{id}  | DELETE           | empty          | Delete position by <code>{id}</code> |
| /positions/{id}  | PUT              | JSON           | Update position by <code>{id}</code> |
| /positions/bulk  | POST             | JSON Array     | Create bulk positions                |

### Frontend: Redis client written in Typescript
> using redis-smq package