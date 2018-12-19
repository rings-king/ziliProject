# 应用名称
RESOURCESSERVICE
# 简介
资源微服务：与物料、机台、工艺参数等相关的功能

# zuul路由前缀
http://192.168.1.37:5555/api/

公网IP映射

http://60.191.71.99:5555/api/

# API清单

## [GET] v1/materials
取所有物料清单

支持分页查询

    请求：(get)http://192.168.1.37:5555/api/v1/materials?page=1&size=3
    注:参数page从1开始,但是返回的"number"是从0开始
    回应:
    {
        "content": [
            {
                "materialId": 16,
                "organizationId": 12,
                "materialName": "香飘飘大号盖子",
                "materialCode": "15232",
                "materialModel": null,
                "materialComment": "一个大号塑料盖子用于包装香飘飘"
            },
            {
                "materialId": 17,
                "organizationId": 12,
                "materialName": "香飘飘小号杯子",
                "materialCode": "15233",
                "materialModel": null,
                "materialComment": "一个小号纸杯用于包装香飘飘"
            },
            {
                "materialId": 18,
                "organizationId": 12,
                "materialName": "香飘飘小号杯子",
                "materialCode": "15234",
                "materialModel": null,
                "materialComment": "一个大号纸杯用于包装香飘飘"
            }
        ],
        "last": false,
        "totalElements": 13,
        "totalPages": 5,
        "sort": null,
        "first": true,
        "numberOfElements": 3,
        "size": 3,
        "number": 0
    }
    http status:
    200 OK
    204 没有查询到记录
    404 服务未启动
    500 数据库连接错误

## [GET] /v1/materials/{id}
根据{id}查询物料

    请求：（get）http://192.168.1.37:5555/api/v1/materials/1
    回应：
    {
        "materialId": 1,
        "organizationId": 10,
        "materialName": "香飘飘盖子",
        "materialCode": "150",
        "materialModel": null,
        "materialComment": "一片塑料盖子用于包装香飘飘"
    }
    http status:
    200 OK
    204 没有查询到记录
    404 服务未启动
    500 数据库连接错误

## [GET] /v1/materials/getbyorganizationid/{organizationId}
根据组织ID{organizationId}来查询该组织的所有物料

支持分页查询如

/v1/materials/getbyorganizationid/124?page=0&size=3
注:参数page从1开始,但是返回的"number"是从0开始

    请求：（get）http://192.168.1.37:5555/api/v1/materials/getbyorganizationid/12
    回应：
        {
            "content": [
                {
                    "materialId": 28,
                    "organizationId": 124,
                    "materialName": "香飘飘小号盖子",
                    "materialCode": "15231",
                    "materialModel": "Xsmall",
                    "materialComment": "一个小号塑料盖子用于包装香飘飘"
                },
                {
                    "materialId": 29,
                    "organizationId": 124,
                    "materialName": "香飘飘大号盖子",
                    "materialCode": "15232",
                    "materialModel": "Xsmall",
                    "materialComment": "一个大号塑料盖子用于包装香飘飘"
                },
                {
                    "materialId": 30,
                    "organizationId": 124,
                    "materialName": "香飘飘小号杯子",
                    "materialCode": "15233",
                    "materialModel": "Xsmall",
                    "materialComment": "一个小号纸杯用于包装香飘飘"
                }
            ],
            "last": false,
            "totalElements": 4,
            "totalPages": 2,
            "sort": null,
            "first": true,
            "numberOfElements": 3,
            "size": 3,
            "number": 0
        }
    http status:
    200 OK
    204 没有查询到记录
    404 服务未启动
    500 数据库连接错误

## [POST] /v1/materials/
添加一个物料

    请求：（post Content-Type: application/json）
    http://192.168.1.37:5555/api/v1/materials
    {
        "organizationId": 10,
        "materialName": "香飘飘小号杯子",
        "materialCode": "151",
        "materialModel": "Xsmall",
        "materialComment": "一个小号纸盖子用于包装香飘飘"
    }
    回应:
    http status:
    201 添加成功,添加成功时会返回此次添加的物料对象
    422 收到的数据有误，json格式错误或者materialName\organizationId是空的
    400 json格式错误，或者字段类型错误
    500 其它内部错误

## [POST] /v1/materials/bulksavebyorganizationid/{organizationId}
导入一批物料到某个组织Id，会删除之前存在的该组织的所有物料

## [POST] /v1/materials/bulksave/organizationid/{organizationId}
导入一批物料到某个组织Id，不删除之前存在的该组织的所有物料

## [POST] /v1/materials/bulksave
导入一批物料，不删除之前存在的该组织的所有物料

    请求：（post Content-Type: application/json）
    http://192.168.1.37:5555/api/v1/materials/bulksavebyorganizationid/124
    [{
        "materialName": "香飘飘小号盖子",
        "materialCode": "15231",
        "materialModel": "Xsmall",
        "materialComment": "一个小号塑料盖子用于包装香飘飘"
    },
    {
        "materialName": "香飘飘大号盖子",
        "materialCode": "15232",
        "materialModel": "Xsmall",
        "materialComment": "一个大号塑料盖子用于包装香飘飘"
    },
    {
        "materialName": "香飘飘小号杯子",
        "materialCode": "15233",
        "materialModel": "Xsmall",
        "materialComment": "一个小号纸杯用于包装香飘飘"
    },
    {
        "materialName": "香飘飘小号杯子",
        "materialCode": "15234",
        "materialModel": "Xsmall",
        "materialComment": "一个大号纸杯用于包装香飘飘"
    }]
    回应：
    http status:
    201 添加成功
    422 收到的数据有误，json格式错误或者materialName\organizationId是空的
    400 json格式错误，或者字段类型错误
    500 其它内部错误



## [DELETE] /v1/materials/{id}
根据{id}删除物料

    请求:(delete)
    http://192.168.1.37:5555/api/v1/materials/49
    回应:
    http status:
    200 删除成功, 同时返回被删除掉的material对象
    422 要删除的数据不存在
    500 其它内部错误

## [PUT] /v1/materials/{id}
根据{id}更新物料

    请求:(PUT)
    http://192.168.1.37:5555/api/v1/materials/44
    {
      "materialName": "匠兴三驴大号盖子"
    }

    回应:
    http status:
    200 更新成功, 同时返回更新后的material对象
    422 要更新的数据不存在
    500 其它内部错误

## [DELETE] /v1/materials/organizationid/{organizationid}
删除指定组织的所有物料

    请求:(delete)
    http://192.168.1.37:5555/api/v1/materials/organizationid/3021
    回应:
    http status:
    200 删除成功
    422 要删除的数据不存在
    500 其它内部错误