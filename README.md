# Fullstack Redis project (Spring Java / Angular)
### Backend: SpringBoot Redis 
#### REST API

| Endpoint<br/>URI               | HTTP <br/>method | Post <br/>body | Action                                              |
|--------------------------------|------------------|----------------|-----------------------------------------------------|
| /api/positions                 | GET              | empty          | Find all positions                                  |
| /api/positions/{id}            | GET              | empty          | Find position by <code>{id}</code>                  |
| /api/positions/userid/{userId} | GET              | empty          | Find all positions owned by <code>{userId}</code>   |
| /api/positions                 | POST             | JSON           | Create new position                                 |
| /api/positions/{id}            | DELETE           | empty          | Delete position by <code>{id}</code>                |
| /api/positions/userid/{userId} | DELETE           | empty          | Delete all positions owned by <code>{userId}</code> |
| /api/positions/{id}            | PUT              | JSON           | Update position by <code>{id}</code>                |
| /api/positions/bulk            | POST             | JSON Array     | Create bulk positions                               |

### Frontend: Angular 12