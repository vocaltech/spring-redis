# Fullstack Redis project (Spring Java / Typescript)
### Backend: SpringBoot Redis 
#### REST API

| URI              | HTTP <br/>method | Post <br/>body | Result                 |
|------------------|------------------|----------------|------------------------|
| /positions       | GET              | empty          | Find all positions     |
| /positions/{id}  | GET              | empty          | Find position by id    |
| /positions       | POST             | JSON           | Create new position    |
| /positions/{id}  | DELETE           | empty          | Delete position by id  |
| /positions/{id}  | PUT              | JSON           | Update position by id  |

### Frontend: Redis client written in Typescript
> using redis-smq package