# zuul前缀
本机地址：http://192.168.1.118:5555/api/

服务器地址：http://192.168.1.37:5555/api/

# 用户模块
# API清单:
## 1.注册用户
    [post]uaa/v1/users/registerUser
    请求地址：http://192.168.1.118:5555/api/uaa/v1/users/registerUser
    参数类型：Json
    例子：{
        "username":"张三",
        "password":"123456",
        "realName":"真张三",
        "phoneNum":"18858888154",
        "email":"123456@163.com"
    }
    返回值：
    200 用户注册成功

## 2.用户登录
    [post]api/uaa/oauth/token?grant_type=password
    请求地址:http://192.168.1.118:5555/api/uaa/oauth/token?grant_type=password&username=张三&password=123456
    返回值样例：
    一：
    {
        "access_token": "af493e5f-4478-4955-9567-7930652ab3d7",
        "token_type": "bearer",
        "refresh_token": "6fe4024a-3bca-472d-9781-ee575241bdb3",
        "expires_in": 43129,
        "scope": "xx"
    }表示登录成功

    二：
    {
        "error": "invalid_grant",
        "error_description": "Bad credentials"
    }表示登录失败

注意：除了注册登录和验证码，目前请求其它接口均要携带access_token
## 3.用户修改密码
    [PUT]uaa/v1/users/modifyPassword
    请求地址：http://192.168.1.118:5555/api/uaa/v1/users/modifyPassword?access_token=af493e5f-4478-4955-9567-7930652ab3d7
    参数类型:Json
    例子：{
        "password":"xxx",
        "newPassword":"12345"
    }
    返回值类型：Json
    一:{
          "error": "原密码输入错误"
      }
    二：{
          "success": true
      }
    三：{
          "success": false
      }

## 4.用户修改邮箱
    [PUT]uaa/v1/users/modifyEmail
    请求地址：http://192.168.1.118:5555/api/uaa/v1/users/modifyEmail?access_token=af493e5f-4478-4955-9567-7930652ab3d7
    参数类型:Json
    例子：{
        "newEmail":"12345@qq.com"
    }
    返回值类型：Json
    一：{
          "error": "新邮箱不能为空"
      }
    二：{
          "error": false 表示修改失败
      }
    三：{
          "success": true
      }

## 5.生成验证码
    [GET]uaa/v1/users/valiCode
    请求地址：http://192.168.1.118:5555/api/uaa/v1/users/valiCode
    返回值：没有返回值，直接渲染页面生存验证码

## 6.校对验证码
    [GET]uaa/v1/users/proofCheckCode
    请求地址：http://192.168.1.118:5555/api/uaa/v1/users/proofCheckCode
    参数类型：String
    例子:$.ajax({
                url: 'http://localhost:5555/api/uaa/v1/users/proofCheckCode',
                type: 'GET',
                data: {vailCode: $("#code").val()},
                success: function(data) {
                    console.log(data);
                },
                xhrFields: {
                  withCredentials: true
                }
            })
    注意：请开启xhrFields，ajax要开启跨于请求
    返回值：

## 7.判断用户名是否存在
    [GET]uaa/v1/users/isExistsUserName
    请求地址：http://192.168.1.118:5555/api/uaa/v1/users/isExistsUserName?userName=jux
    返回值类型：Boolean
    {success : true } 表示用户名已存在
    {error : false} 表示用户名不存在

## 8.判断邮箱是否已存在
    [GET]uaa/v1/users/isExistsEmail
    请求地址：http://192.168.1.118:5555/api/uaa/v1/users/isExistsEmail?email=123456@163.com
    返回值类型：Json
    一：
    {
        "error": "邮箱不能为空"
    }
    二：
    {
        "error": "邮箱已存在"
    }
    三：
    {
        "success": "true"
    }
## 9.获取所有用户的信息详情
    [GET]uaa/v1/users/getUserList
    请求地址：http://192.168.1.112:5555/api/uaa/v1/users?sort=id,asc&access_token=b59ae24b-9f2c-4aac-b3a0-a8d76dfa906e
    返回值类型：Json
    一：{
            "error": "服务器错误"
        }
    二：{
    "content":[
    {"id": 5, "username": "admin", "password": "$2a$10$I2Wv63mAQBkJVTC39oDaseYn6gvwcNyHVXs7q/Mdu9SDnQj4P0Zwu",…},
    {"id": 6, "username": "wyf", "password": "$2a$10$H482fI3kQ6DRF9QpUwee6e8vRDsnAreMDU4xKVftNtKKAcnMFWWWO",…},
    {"id": 7, "username": "张三", "password": "$2a$10$M7oKix8BvKzZO9jo/hKwi.pZSAADUmQ9Sz2Vdw6xLb08WV97nJndi",…},
    {"id": 8, "username": "yuwenwen", "password": "$2a$10$u6nszpe7e4OCKmh0HShFTOIUNvFw1pGgmiQSjfsANuXuhREiysHUi",…},
    {"id": 9, "username": "王五", "password": "$2a$10$Z/VZEbFs7vpFdRPmlDumpehfcCKQBh105DJ0HeoismACJpb08NKXi",…}
    ],
    "last": true,
    "totalElements": 5,
    "totalPages": 1,
    "number": 0,
    "size": 10,
    "sort":[
    {"direction": "ASC", "property": "id", "ignoreCase": false, "nullHandling": "NATIVE",…}
    ],
    "first": true,
    "numberOfElements": 5
    }
## 10.管理员添加用户（包括设置角色，部门）
      [POST]uaa/v1/users
      请求地址：http://192.168.1.112:5555/api/uaa/v1/users?access_token=b59ae24b-9f2c-4aac-b3a0-a8d76dfa906e
      返回值类型：Boolean
       一：{
              "error": "用户已存在"
        }
       二：{
              "error": "邮箱已存在"
        }
       三：{
              "error": "内容不能为空"
        }
       四：{
               "error": "用户保存失败"
        }
       五：{
               "success": "true"
        }
       新增用户默认登陆密码123456
## 11.判断手机号是否存在
    [GET]uaa/v1/users/isExistsPhone
    请求地址：http://192.168.1.112:5555/api/uaa/v1/users/isExistsPhone?phoneNum=11111221111
    返回值类型：Boolean
    {success : true } 表示手机号已存在
    {error : false} 表示手机号不存在
## 12.管理员修改用户信息（包括修改角色，部门）
    [PUT]uaa/v1/users/isExistsPhone
    请求地址：http://192.168.1.112:5555/api/uaa/v1/users/7?access_token=afe259c7-a6f8-447b-86f4-aaeb8f9549d1
    返回值类型：Boolean
    一：{
          "error": "用户名已存在"
    }
    二：{
          "error": "手机号已存在"
    }
    三：{
          "error": "邮箱已存在"
    }
    四：{
           "error": "传递json对象有误"
    }
    五：{
           "success": "true"
    } 
    一：{
          "error": "修改没有成功"
    }  
      
## 13.根据id获取用户信息
    [GET]uaa/v1/users 
    请求地址：http://192.168.1.112:5555/api/uaa/v1/users/8?access_token=7d422b0c-4188-4e44-88d9-d6116326c972
    返回值类型：json 
    参数类型：int
    例子:
    一：{
         "error": "查询出错,用户不存在，请检查用户id"
    }  
    二： {
    "id": 8,
    "realName": "于文文",
    "password": null,
    "phoneNum": "12345678901",
    "email": "1111111@qq.com",
    "userDepartName": "生产部",
    "username": "yuwenwen456",
    "roleId": null,
    "roles":[]
    }
## 14.功能列表展示
    [GET]uaa/v1/users/getAllGnb
    请求地址：http://192.168.1.112:5555/api/uaa/v1/users/getAllGnb?access_token=7d422b0c-4188-4e44-88d9-d6116326c972
    http://192.168.1.112:5555/api/uaa/v1/users/getAllGnb?menuList=1,3&access_token=7d422b0c-4188-4e44-88d9-d6116326c972
    地址后可以拼接menuTlist= 1   / menuTlist= 1,3 / menuTlist= 1,0 ..............
    不拼接表示查询全部
    返回值类型：json 
    参数类型：int
     一：{
             "error": "服务器错误"
        } 
    二：[
    {
    "sjgbnm": null,
    "gnbm": "40",
    "id": 2476,
    "leaf": 0,
    "menutype": null,
    "name": "Mes_mesglxt",
    "regiontype": 0,
    "text": "Mes管理系统",
    "children":[{"sjgbnm": "40", "gnbm": "4001", "id": 2477, "leaf": 0,…]
    }
    ] 
## 15.获取部门信息
     [GET]uaa/v1/departments 
     请求地址：http://192.168.1.112:5555/api/uaa/v1/departments?access_token=f5d961f8-27e9-4280-b7fa-05ef64abd005&page=0 
     返回值类型：json 
     一：{
     "content":[{"id": 2, "departCode": "dept_code004", "departName": "行政部", "departParentId": 1,…],
     "totalPages": 1,
     "totalElements": 2,
     "last": true,
     "number": 0,
     "size": 10,
     "sort":[
     {"direction": "DESC", "property": "id", "ignoreCase": false, "nullHandling": "NATIVE",…}
     ],
     "first": true,
     "numberOfElements": 2
     } 
     二：{
          "error": "服务器错误"
     }           
## 16.添加部门
      [POST]uaa/v1/departments  
      请求地址：http://192.168.1.112:5555/api/uaa/v1/departments?access_token=f5d961f8-27e9-4280-b7fa-05ef64abd005
      返回值类型：json
      参数类型：{"addnew":{"departCode":"11035","departName":"人事部","departParentId":""},"update":{},"remove":{}} 
      一：{
           "error": "服务器错误"
      }
      二：{
            "error": "内容不能为空"
      }
      三：{
             "error": "添加失败,该部门编码已存在"
      }
      四：{
             "error": "添加失败,该部门名称已存在"
      }
      五：{
             "success": true
      }
## 16.修改部门信息
       [PUT]uaa/v1/departments  
       请求地址：http://192.168.1.112:5555/api/uaa/v1/departments?access_token=f5d961f8-27e9-4280-b7fa-05ef64abd005
       返回值类型：json
       参数类型：
       {
           "addnew": {
               "quarterDetailsTab": [
                   {
                       "quarterName": "会计"
                   }
               ]
           },
           "update": {
               "departName": "财务部111",
               "departParentId":1,
               "id": 3
           },
           "remove": {},
           "id": 3
       }
       
        一：{
                  "error": "服务器错误"
        }
        二：{
                   "error": "内容不能为空"
        }
        三：{
                    "error": "上级部门不能和本身相同"
        }
        四：{
                    "error": "用户编号重复"
        }
        五：{
                    "error": "用户名重复"
        }
        五：{
                    "success": true
        }   
## 17.根据id获取该部门信息
       [GET]uaa/v1/departments  
       请求地址：http://192.168.1.112:5555/api/uaa/v1/departments/2?access_token=f5d961f8-27e9-4280-b7fa-05ef64abd005&page=2
       返回值类型：json
        一：{
                  "error": "服务器错误"
        }
        二：{
          "id": 3,
          "departCode": "1002",
          "departName": "财务部111",
          "departParentId": 1,
          "deparParentName": "董事会",
          "quarterList":[
          {
          "id": 21,
          "quarterName": "会计",
          "deptId": 3
          }
          ]
          }
## 18.根据id删除该部门信息
    [DELETE]uaa/v1/departments  
    请求地址：http://192.168.1.112:5555/api/uaa/v1/departments/2?access_token=f5d961f8-27e9-4280-b7fa-05ef64abd005&page=2
    返回值类型：json
     一：{
           "error": "服务器错误"
     }
     二：{
          "success" : true
     }
## 19.添加一个新角色
    [POST]uaa/v1/roles
    请求地址：http://192.168.1.112:5555/api/uaa/v1/roles?access_token=283eaee3-f0aa-4d0f-b895-060602f0f2d9
    返回值类型：json
    参数实例：
        {
         "addnew": {
         "name": "test02",
         "rolePermission": "2477,2661,2662,2663,2664,2665,2666,2667,2669,2670,2671,2672,2673,2679,2681,2682,2683,2684,2685"
         },
         "update": {},
         "remove": {}
         }
     一：{
     "success": true
     }
     二：{
     "error": "用户没有选择所在系统"
     }
     三：{
     "error": 角色已存在请修改
     }
## 20.根据id删除该角色
     [DELETE]uaa/v1/roles    
     请求地址：http://192.168.1.112:5555/api/uaa/v1/roles/5?access_token=9d73ddf8-38ff-4764-b530-ef20107e493a
     返回值类型：json 
     一： 
    {
        "success": true
    }
    二：
    {
            "error": 服务器错误
    }
## 21.获取所有角色 及其权限
    [GET]uaa/v1/roles    
    http://192.168.1.112:5555/api/uaa/v1/roles?access_token=9d73ddf8-38ff-4764-b530-ef20107e493a 
    返回值类型：json 
    一：[
    {
    "id": 14,
    "name": "test02",
    "value": null,
    "rolePermission": "2477,2661,2662,2663,2664,2665,2666,2667,2669,2670,2671,2672,2673,2679,2681,2682,2683,2684,2685",
    "xtgnbList": []
    },
    {
    "id": 15,
    "name": "test03",
    "value": null,
    "rolePermission": "2477,2661,2662,2663,2664,2665,2666,2667,2669,2670,2671,2672,2673,2679,2681,2682,2683,2684,2685,2746",
    "xtgnbList": []
    }
    ]
    二：{
        "error" : "目前没有任何角色，请先添加"
    }
## 22.用户进入系统后的页面功能显示
    [GET]uaa/v1/users 
    请求地址： http://192.168.1.112:5555/api/uaa/v1/users/showUserSysList?access_token=9d73ddf8-38ff-4764-b530-ef20107e493a&systermId=2746&menuList=1,3&gnbm=4001     
    返回值类型：
    一：{
    "sjgbnm": "4001",
    "gnbm": "400101",
    "id": 2661,
    "leaf": 1,
    "menutype": 0,
    "name": "Mes_MaterialType",
    "regiontype": 1,
    "text": "物料分类",
    "children":[{"sjgbnm": "400101", "gnbm": "40010101", "id": 2662, "leaf": 1,…]
    },
    {
    "sjgbnm": "4001",
    "gnbm": "400110",
    "id": 2667,
    "leaf": 1,
    "menutype": 0,
    "name": "Mes_MeasureUnit",
    "regiontype": 1,
    "text": "计量单位",
    "children":[{"sjgbnm": "400110", "gnbm": "40011001", "id": 2669, "leaf": 1,…]
    },
    {
    "sjgbnm": "4001",
    "gnbm": "400112",
    "id": 2679,
    "leaf": 1,
    "menutype": 0,
    "name": "Mes_WorkProcess",
    "regiontype": 1,
    "text": "工艺流程",
    "children":[{"sjgbnm": "400112", "gnbm": "40011201", "id": 2681, "leaf": 1,…]
    }
    ]
## 22.用户进入系统后的页面按钮显示    
     [GET]uaa/v1/users 
     请求地址：http://192.168.1.112:5555/api/uaa/v1/users/showAllButton?access_token=bbe3eff1-e227-4231-83d8-e88db13a1ea3&menuList=1,3&gnbm=400101
     返回值类型：JSON
     {
     "viewlist":[
     {
     "gnbm": "40010101",
     "menutype": 3,
     "scope": "controller",
     "text": "新增",
     "iconCls": "add",
     "handler": "on40010101_click",
     "showbystatus": "view"
     },
     {
     "gnbm": "40010102",
     "menutype": 3,
     "scope": "controller",
     "text": "修改",
     "iconCls": "edit",
     "handler": "on40010102_click",
     "showbystatus": "view"
     },
     {
     "gnbm": "40010103",
     "menutype": 3,
     "scope": "controller",
     "text": "删除",
     "iconCls": "delete",
     "handler": "on40010103_click",
     "showbystatus": "view"
     },
     {
     "gnbm": "40010104",
     "menutype": 3,
     "scope": "controller",
     "text": "保存",
     "iconCls": "save",
     "handler": "on40010104_click",
     "showbystatus": "edit"
     },
     {
     "gnbm": "40010105",
     "menutype": 3,
     "scope": "controller",
     "text": "撤销",
     "iconCls": "exit",
     "handler": "on40010105_click",
     "showbystatus": "edit"
     }
     ],
     "methodlist":[
     {
     "gnbm": "40010101",
     "beforeclick": "this.onAddClick(button);",
     "afterclick": null
     },
     {
     "gnbm": "40010102",
     "beforeclick": "this.changebillState(button,\\\"edit\\\")",
     "afterclick": null
     },
     {
     "gnbm": "40010103",
     "beforeclick": "this.onDelClick(button)",
     "afterclick": null
     },
     {
     "gnbm": "40010104",
     "beforeclick": "this.onSaveClick(button)",
     "afterclick": null
     },
     {
     "gnbm": "40010105",
     "beforeclick": "this.onExitClick(button)",
     "afterclick": null
     }
     ]
     }
     
## 员工管理

## 23.添加员工
    [POST]uaa/v1/employee 
    请求地址：http://192.168.1.112:5555/api/uaa/v1/employee?access_token=bbe3eff1-e227-4231-83d8-e88db13a1ea3     
    {"addnew":{"empNumber":"10009","empName":"text01","quarter":"1"},"update":{},"remove":{}}
     返回值类型：JSON
     一：{
       "success": true
       }
     二：{
       "error": "该员工编码已存在，请重新添加"
      } 
## 24.删除员工
      [DELETE]uaa/v1/employee  
      请求地址：http://192.168.1.112:5555/api/uaa/v1/employee/2?access_token=bbe3eff1-e227-4231-83d8-e88db13a1ea3  
      返回值类型：JSON
      一：{
      "success": true
      }
      二：{
      "error": "服务器错误"
      }
      三：{
            "error": "该部门下存在员工或用户不允许删除"
      }
      
## 25.修改员工
       [PUT]uaa/v1/employee    
       请求地址：http://192.168.1.112:5555/api/uaa/v1/employee?access_token=bbe3eff1-e227-4231-83d8-e88db13a1ea3
       {"update":{"empNumber":"10009","empName":"text01","quarter":"1","id":1},"addnew":{},"remove":{}}
       一：{
         "error": "该员工编码已存在，请重新添加"
         }
       二：{
          "error": "修改失败"
       } 
       三：{
           "success": true
       }
## 26.获取员工档案
      [GET]uaa/v1/employee 
      请求地址：http://192.168.1.112:5555/api/uaa/v1/employee?access_token=bbe3eff1-e227-4231-83d8-e88db13a1ea3   
      返回值类型：JSON
      一：{
      "content":[
      {
      "id": 3,
      "empNumber": "10010",
      "empName": "text02",
      "quarter": "Java程序员",
      "department": "质量部"
      },
      {
      "id": 1,
      "empNumber": "10009",
      "empName": "text01",
      "quarter": "产品经理",
      "department": "财务部"
      }
      ],
      "last": true,
      "totalElements": 2,
      "totalPages": 1,
      "number": 0,
      "size": 0,
      "sort": null,
      "first": true,
      "numberOfElements": 2
      }
      二：{
           "error": "服务器错误"
      }
## 27.根据员工id获取员工档案 
       [GET]uaa/v1/employee
       请求地址：http://192.168.1.112:5555/api/uaa/v1/employee/1?access_token=bbe3eff1-e227-4231-83d8-e88db13a1ea3          
       返回值类型：JSON
        {
        "id": 1,
        "empNumber": "10009",
        "empName": "text01",
        "quarter": "产品经理",
        "department": "财务部"
        }
    
## 28.获取所有部门名称：员工档案管理使用
      [GET]uaa/v1/departments
      请求地址：http://192.168.1.112:5555/api/uaa/v1/departments/getDeptName?access_token=0778d232-e8fe-414b-8816-0aea8144cc98
      返回值类型：JSON
       一：[
       {
       "departmentId": 1,
       "departmentName": "董事会"
       },
       {
       "departmentId": 3,
       "departmentName": "财务部"
       },
       {
       "departmentId": 4,
       "departmentName": "人事部"
       },
       {
       "departmentId": 5,
       "departmentName": "质量部"
       },
       {
       "departmentId": 6,
       "departmentName": "研发部"
       }
       ]
       二：{
             "error": "服务器错误"
       }
## 29.根据部门id获取其下所有岗位
      [GET]uaa/v1/departments
      请求地址：http://192.168.1.112:5555/api/uaa/v1/departments/getQuarter/1?access_token=0778d232-e8fe-414b-8816-0aea8144cc98
      返回值类型： JSON
      一： [
      {
      "id": 1,
      "quarterName": "产品经理",
      "deptId": 1
      },
      {
      "id": 3,
      "quarterName": "项目经理",
      "deptId": 1
      }
      ]       
      二：{
          "error": "服务器错误"
      }
      二：{
          "error": "该部门下没有岗位"
      }
    
    
    
    
    
    
    
    
    