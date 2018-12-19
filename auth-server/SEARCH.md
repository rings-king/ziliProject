# zuul前缀
本机地址：http://192.168.1.112:5555/api/

服务器地址：http://192.168.1.37:5555/api/

# 用户模块
# API清单:
## 1.用户搜索
    [GET]uaa/v1/users/searchUser
    请求地址：http://192.168.1.112:5555/api/uaa/v1/users/searchUser?access_token=35c7cbbb-fed2-4e1e-a5e8-873c5e7a2ef9
    参数类型：Json
    例子：[{"fieldname": "username",
       "operator": "like |%",
       "tablename": "Mes_UserManagement_Mes_UserManagementsStore",
       "value": "t"}]
    返回值：{
        "content":[
        {"id": 14, "username": "test1", "realName": "于", "phoneNum": "18270839470",…},
        {"id": 15, "username": "test0102", "realName": "于", "phoneNum": "18696358274",…},
        {"id": 38, "username": "test02", "realName": "ww", "phoneNum": "18252522525",…}
        ],
        "size": 20,
        "page": 1,
        "totalPages": 1,
        "totalElements": 3
        }
        
     二：{
     "error": "该条件无效"
     }
     三：
## 2.角色搜索
    [GET]uaa/v1/roles/searchRole
    请求地址：http://192.168.1.112:5555/api/uaa/v1/roles/searchRole?access_token=35c7cbbb-fed2-4e1e-a5e8-873c5e7a2ef9
    参数类型：Json
    用例：[{"fieldname": "name",
       "operator": "like |%",
       "tablename": "Mes_RoleManagement_Mes_RoleManagementsStore",
       "value": "测"}]
    返回值：{
        "content":[
        {"id": 21, "name": "测试", "value": "测试人员角色", "rolePermission": "2555,2557,2708,2709,2710,2711,2712,2559,2686,2687,2688,2689,2690,2693,2694,2696,2697,2698,2699,2700,2695,2713,2714,2715,2716,2717,2707,2718,2719,2720,2721,2722",…}
        ],
        "size": 20,
        "page": 1,
        "totalPages": 1,
        "totalElements": 1
        }
      二：{
          "error": "该条件无效"
          }
## 3.员工搜索
    [GET]uaa/v1/employee/searchEmp
    请求地址：http://192.168.1.112:5555/api/uaa/v1/employee/searchEmp?access_token=35c7cbbb-fed2-4e1e-a5e8-873c5e7a2ef9
    参数类型：Json
    用例：[{"fieldname": "empName",
       "operator": "=",
       "tablename": "Mes_person_Mes_personsStore",
       "value": "于文文"}]
    返回值：{
        "content":[
        {"id": 5, "empNumber": "10012", "empName": "于文文", "quarter": "实施人员2256",…}
        ],
        "size": 20,
        "page": 1,
        "totalPages": 1,
        "totalElements": 1
        }
      二：{
          "error": "该条件无效"
          }    
## 4.部门搜索
     [GET]uaa/v1/departments/searchDept
     请求地址：http://192.168.1.112:5555/api/uaa/v1/departments/searchDept?access_token=35c7cbbb-fed2-4e1e-a5e8-873c5e7a2ef9
     参数类型：Json
     用例：[{"fieldname": "departName",
        "operator": "like %|",
        "tablename": "Mes_Department_Mes_DepartmentsStore",
        "value": "部"}]
     返回值：{
         "content":[
         {"id": 5, "departCode": "1004", "departName": "质量部", "departParentId": 1,…},
         {"id": 6, "departCode": "1005", "departName": "研发部", "departParentId": null,…},
         {"id": 7, "departCode": "1009", "departName": "测试部", "departParentId": 6,…},
         {"id": 21, "departCode": "100111", "departName": "科质部", "departParentId": 1,…},
         {"id": 22, "departCode": "1001112", "departName": "实施部", "departParentId": 1,…},
         {"id": 24, "departCode": "10012", "departName": "市场部", "departParentId": 1,…}
         ],
         "size": 20,
         "page": 1,
         "totalPages": 1,
         "totalElements": 6
         }
       二：{
           "error": "该条件无效"
           }       
## 5.存入自定义条件
    [POST]uaa/v1/departments/saveCondition
    请求地址：http://192.168.1.112:5555/api/uaa/v1/users/saveCondition?access_token=a3aad345-3ae9-4faa-a933-72820b788b2f
    参数类型：Json
    用例：{"condition":[{"fieldname": "empName",
       "operator": "=",
       "tablename": "Mes_person_Mes_personsStore",
       "value": "hh"}],
        "tablename":"ddy"   这个字段表示用户在哪个模快查询
       }
    返回值：{
        "success": "已保存"
        }
    二：{
               "error": "服务器异常"
     }       
## 6.载入自定义条件    
    [GET]uaa/v1/departments/getCondition
    请求地址：http://192.168.1.112:5555/api/uaa/v1/users/getCondition?access_token=a3aad345-3ae9-4faa-a933-72820b788b2f&tableName=ddy
    返回值：{
          [{"fieldname":"empName","tablename":"Mes_person_Mes_personsStore","value":"hh","operator":"="}]
    }
    二：{
    "error":"服务器错误"
    }
    
    
    