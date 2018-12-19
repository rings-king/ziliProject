/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.jux.mtqiushui.dispatching.controllers;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.jux.mtqiushui.dispatching.model.Assignment;
import com.jux.mtqiushui.dispatching.model.Calendar;
import com.jux.mtqiushui.dispatching.model.Day;
import com.jux.mtqiushui.dispatching.model.Dependency;
import com.jux.mtqiushui.dispatching.model.GanttChart;
import com.jux.mtqiushui.dispatching.model.MetaData;
import com.jux.mtqiushui.dispatching.model.Resource;
import com.jux.mtqiushui.dispatching.model.Rows;
import com.jux.mtqiushui.dispatching.model.TaskTree;
import com.jux.mtqiushui.dispatching.services.AssignmentService;
import com.jux.mtqiushui.dispatching.services.DependencyService;
import com.jux.mtqiushui.dispatching.services.ResourceService;
import com.jux.mtqiushui.dispatching.services.TaskTreeService;

import java.io.IOException;
import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author jux
 */
@RestController
@RequestMapping(value = "v1/tasktrees")
public class GanttChartController {

    @Autowired
    private TaskTreeService taskTreeService;
    @Autowired
    private AssignmentService assignmentService;
    @Autowired
    private ResourceService resourceService;
    @Autowired
    private DependencyService dependencyService;

    private static final Logger LOGGER = LoggerFactory.getLogger(GanttChartController.class);

    /**
     * 主页显示派工详情
     *
     * @param startDate
     * @param endDate
     * @return
     */
    @RequestMapping(value = "daterange/{startDate}/{endDate}", method = RequestMethod.GET)
    public ResponseEntity<GanttChart> getTaskTreesByDateRange(@PathVariable("startDate") Timestamp startDate, @PathVariable("endDate") Timestamp endDate) {

        LOGGER.info(String.format("tasktree-service getTaskTreesByDateRange() 被调用: {%s} 参数 {%s, %s}", taskTreeService.getClass().getName(), startDate, endDate));

        List<TaskTree> taskTrees = null;
        try {
            taskTrees = taskTreeService.getTaskTreesByDateRange(startDate, endDate);

        } catch (Exception ex) {
//            throw new  RuntimeException("查询异常");
//            String errMessage = String.format("调用异常taskTrees：getTaskTreesByDateRange(),错误信息：{%s}", ex.toString());
//            LOGGER.error(errMessage);
//            GanttChart ganttChart = new GanttChart();
//            ganttChart.setSuccess(Boolean.TRUE);
//            ganttChart.setErrorMessage(errMessage);
//            return new ResponseEntity<>(ganttChart, HttpStatus.INTERNAL_SERVER_ERROR);
        }
        //   return taskTrees;
        Iterable<Assignment> assignments;
        try {
            assignments = assignmentService.getAllAssignment();
        } catch (Exception ex) {
            String errMessage = String.format("调用异常taskTrees：getTaskTreesByDateRange(),错误信息：{%s}", ex.toString());
            LOGGER.error(errMessage);
            GanttChart ganttChart = new GanttChart();
            ganttChart.setSuccess(Boolean.TRUE);
            ganttChart.setErrorMessage(errMessage);
            return new ResponseEntity<>(ganttChart, HttpStatus.INTERNAL_SERVER_ERROR);
        }

        Iterable<Resource> resources;
        try {
            resources = resourceService.getAllResource();
        } catch (Exception ex) {
            String errMessage = String.format("调用异常taskTrees：getTaskTreesByDateRange(),错误信息：{%s}", ex.toString());
            LOGGER.error(errMessage);
            GanttChart ganttChart = new GanttChart();
            ganttChart.setSuccess(Boolean.TRUE);
            ganttChart.setErrorMessage(errMessage);
            return new ResponseEntity<>(ganttChart, HttpStatus.INTERNAL_SERVER_ERROR);
        }

        Iterable<Dependency> dependencies;
        try {
            dependencies = dependencyService.getAll();
        } catch (Exception ex) {
            String errMessage = String.format("调用异常taskTrees：getTaskTreesByDateRange(),错误信息：{%s}", ex.toString());
            LOGGER.error(errMessage);
            GanttChart ganttChart = new GanttChart();
            ganttChart.setSuccess(Boolean.TRUE);
            ganttChart.setErrorMessage(errMessage);
            return new ResponseEntity<>(ganttChart, HttpStatus.INTERNAL_SERVER_ERROR);
        }

        Rows<List<TaskTree>> taskTreeRows = new Rows();
        taskTreeRows.setRows(taskTrees);

        Rows<Iterable<Assignment>> assignmentRows = new Rows();
        assignmentRows.setRows(assignments);

        Rows<Iterable<Resource>> resourceRows = new Rows();
        resourceRows.setRows(resources);

        Rows<Iterable<Dependency>> dependencyRows = new Rows();
        dependencyRows.setRows(dependencies);

        GanttChart ganttChart = new GanttChart();
        ganttChart.setSuccess(Boolean.TRUE);

        ganttChart.setAssignments(assignmentRows);
        ganttChart.setCalendars(calendarsRender());
        ganttChart.setResources(resourceRows);
        ganttChart.setDependencies(dependencyRows);
        ganttChart.setTasks(taskTreeRows);
        //return taskTrees != null ? new ResponseEntity<>(taskTrees, HttpStatus.OK) : new ResponseEntity<>(HttpStatus.NO_CONTENT);
        return new ResponseEntity<>(ganttChart, HttpStatus.OK);
    }

    private Rows<List<Calendar>> calendarsRender() {
        //手工生成一个calendars出来

        Rows<List<Calendar>> calendarRows = new Rows();
        MetaData metaData = new MetaData();
        metaData.setProjectCalendar("default");
        calendarRows.setMetaData(metaData);

        Calendar calendar1 = new Calendar();
        calendar1.setId("default");
        calendar1.setName("Default");
        calendar1.setDaysPerWeek(5);
        calendar1.setDaysPerMonth(20);
        calendar1.setHoursPerDay(8);
        calendar1.setWeekendFirstDay(6);
        calendar1.setWeekendSecondDay(0);
        calendar1.setWeekendsAreWorkDays(Boolean.FALSE);
        calendar1.setDefaultAvailability(new String[]{"08:00-12:00", "13:00-17:00"});
        calendar1.setLeaf(Boolean.TRUE);

        Day day1 = new Day();
        day1.setCls("gnt-national-holiday");
        String timeStr = "2010-01-12 00:00:00";
        day1.setDate(Timestamp.valueOf(timeStr));
        day1.setId(new Long(1));
        day1.setCalendarId("default");
        day1.setName("Some big holiday");

        List<Day> days = new ArrayList();
        days.add(day1);
        Day day2 = new Day();
        day2.setCls("gnt-chinese-holiday");
        timeStr = "2010-02-14 00:00:00";
        day2.setDate(Timestamp.valueOf(timeStr));
        day2.setId(new Long(2));
        day2.setCalendarId("default");
        day2.setName("Chinese New Year");
        days.add(day2);

        Rows<List<Day>> dayRows = new Rows();
        dayRows.setRows(days);

        calendar1.setDays(dayRows);
        //第1个calendar手工做完

        List<Calendar> calendars = new ArrayList();
        calendars.add(calendar1);

        Calendar calendar2 = new Calendar();
        calendar2.setId("NightShift");
        calendar2.setName("Night Shift");
        calendar2.setDaysPerWeek(5);
        calendar2.setDaysPerMonth(20);
        calendar2.setHoursPerDay(8);
        calendar2.setWeekendFirstDay(6);
        calendar2.setWeekendSecondDay(0);
        calendar2.setWeekendsAreWorkDays(Boolean.FALSE);
        calendar2.setDefaultAvailability(new String[]{"00:00-16:00", "22:00-24:00"});
        calendar2.setLeaf(Boolean.TRUE);
        calendars.add(calendar2);

        calendarRows.setRows(calendars);

        return calendarRows;
    }

    //@RequestMapping(value = "save", method = RequestMethod.POST, consumes = "text/plain")
    @RequestMapping(value = "save", method = RequestMethod.POST)
    @Transactional
    public ResponseEntity<Map> saveTaskTrees(@RequestBody String porridge) {
        try {
            LOGGER.info(String.format("tasktree-service saveTaskTrees() 被调用: {%s}", porridge));
            ObjectMapper objectMapper = new ObjectMapper();
            //粥转成粥Map
            Map porridgeMap;
            porridgeMap = objectMapper.readValue(porridge, Map.class);

            //从粥Map里把requestId取出来，生成responseMap
            Long requestId = Long.parseLong(porridgeMap.get("requestId").toString());
            /////////////////////////////////////////////////////////////////////
            //从粥Map里把assignments取出来，处理其中的added, updated, removed列表
            /////////////////////////////////////////////////////////////////////
            Map assignmentsMap = (HashMap) porridgeMap.get("assignments");
            if (assignmentsMap != null) {
                //将assignments的added取出为List
                List<Map> assignmentsListMap = (List<Map>) assignmentsMap.get("added");

                if (assignmentsListMap != null) {
                    List<Assignment> assignmentsList = createAssignmentsList(assignmentsListMap);
                    try {
                        //将assignments added列表存入数据库
                        assignmentService.addList(assignmentsList);
                    } catch (Exception ex) {
                        String errMessage = String.format("调用异常：saveTaskTrees(),错误信息：{%s}", ex.toString());
                        LOGGER.error(errMessage);
                        Map responseMap = new HashMap();
                        responseMap.put("success", false);
                        responseMap.put("requestId", requestId);
                        responseMap.put("errorMessage", errMessage);
                        return new ResponseEntity<>(responseMap, HttpStatus.INTERNAL_SERVER_ERROR);
                    }
                }

                assignmentsListMap = (List<Map>) assignmentsMap.get("updated");
                if (assignmentsListMap != null) {
                    List<Assignment> assignmentsList = createAssignmentsList(assignmentsListMap);
                    try {
                        //将assignments updated列表更新到数据库
                        assignmentService.updateList(assignmentsList);
                    } catch (Exception ex) {
                        String errMessage = String.format("调用异常：saveTaskTrees(),错误信息：{%s}", ex.toString());
                        LOGGER.error(errMessage);
                        Map responseMap = new HashMap();
                        responseMap.put("success", false);
                        responseMap.put("requestId", requestId);
                        responseMap.put("errorMessage", errMessage);
                        return new ResponseEntity<>(responseMap, HttpStatus.INTERNAL_SERVER_ERROR);
                    }
                }

                assignmentsListMap = (List<Map>) assignmentsMap.get("removed");
                if (assignmentsListMap != null) {
                    List<Assignment> assignmentsList = createAssignmentsList(assignmentsListMap);
                    try {
                        assignmentService.deleteList(assignmentsList);
                    } catch (Exception ex) {
                        String errMessage = String.format("调用异常：saveTaskTrees(),错误信息：{%s}", ex.toString());
                        LOGGER.error(errMessage);
                        Map responseMap = new HashMap();
                        responseMap.put("success", false);
                        responseMap.put("requestId", requestId);
                        responseMap.put("errorMessage", errMessage);
                        return new ResponseEntity<>(responseMap, HttpStatus.INTERNAL_SERVER_ERROR);
                    }
                }
            }

            /////////////////////////////////////////////////////////////////////
            //从粥Map里把dependencies取出来，处理其中的added, updated, removed列表
            /////////////////////////////////////////////////////////////////////
            Map dependenciesMap = (HashMap) porridgeMap.get("dependencies");
            if (dependenciesMap != null) {
                //将dependencies的added取出为List
                List<Map> dependenciesMapList = (List<Map>) dependenciesMap.get("added");

                if (dependenciesMapList != null) {
                    List<Dependency> dependenciesList = createDependenciesList(dependenciesMapList);
                    try {
                        //将assignments added列表存入数据库
                        dependencyService.addList(dependenciesList);
                    } catch (Exception ex) {
                        String errMessage = String.format("调用异常：saveTaskTrees(),错误信息：{%s}", ex.toString());

                        LOGGER.error(errMessage);
                        Map responseMap = new HashMap();
                        responseMap.put("success", false);
                        responseMap.put("requestId", requestId);
                        responseMap.put("errorMessage", errMessage);
                        return new ResponseEntity<>(responseMap, HttpStatus.INTERNAL_SERVER_ERROR);
                    }
                }

                dependenciesMapList = (List<Map>) dependenciesMap.get("updated");
                if (dependenciesMapList != null) {
                    List<Dependency> dependenciesList = createDependenciesList(dependenciesMapList);
                    try {
                        //将assignments updated列表更新到数据库
                        dependencyService.addList(dependenciesList);
                    } catch (Exception ex) {
                        String errMessage = String.format("调用异常：saveTaskTrees(),错误信息：{%s}", ex.toString());

                        LOGGER.error(errMessage);
                        Map responseMap = new HashMap();
                        responseMap.put("success", false);
                        responseMap.put("requestId", requestId);
                        responseMap.put("errorMessage", errMessage);
                        return new ResponseEntity<>(responseMap, HttpStatus.INTERNAL_SERVER_ERROR);
                    }
                }

                dependenciesMapList = (List<Map>) dependenciesMap.get("removed");
                if (dependenciesMapList != null) {
                    List<Dependency> dependenciesList = createDependenciesList(dependenciesMapList);
                    try {
                        dependencyService.deleteList(dependenciesList);
                    } catch (Exception ex) {
                        String errMessage = String.format("调用异常：saveTaskTrees(),错误信息：{%s}", ex.toString());

                        LOGGER.error(errMessage);
                        Map responseMap = new HashMap();
                        responseMap.put("success", false);
                        responseMap.put("requestId", requestId);
                        responseMap.put("errorMessage", errMessage);
                        return new ResponseEntity<>(responseMap, HttpStatus.INTERNAL_SERVER_ERROR);
                    }
                }
            }

            /////////////////////////////////////////////////////////////////////
            //从粥Map里把 tasks 取出来，处理其中的added, updated, removed列表
            /////////////////////////////////////////////////////////////////////
            Map tasksMap = (HashMap) porridgeMap.get("tasks");
            if (tasksMap != null) {
                //将dependencies的added取出为List
                List<Map> tasksMapList = (List<Map>) tasksMap.get("added");

                if (tasksMapList != null) {
                    List<TaskTree> tasksList;

                    tasksList = createTaskTreesList(tasksMapList);
                    for (TaskTree task : tasksList) {
                        if (task.getBaselineEndDate() == null || task.getBaselineStartDate() == null) {
                            String errMessage = "调用异常：saveTaskTrees(),错误信息：add时baseline日期为空";
                            LOGGER.error(errMessage);
                            Map responseMap = new HashMap();
                            responseMap.put("success", false);
                            responseMap.put("requestId", requestId);
                            responseMap.put("errorMessage", errMessage);
                            return new ResponseEntity<>(responseMap, HttpStatus.INTERNAL_SERVER_ERROR);
                        }
                    }

                    try {
                        //将assignments added列表存入数据库
                        taskTreeService.saveList(tasksList);
                    } catch (Exception ex) {
                        String errMessage = String.format("调用异常：saveTaskTrees(),错误信息：{%s}", ex.toString());
                        LOGGER.error(errMessage);
                        Map responseMap = new HashMap();
                        responseMap.put("success", false);
                        responseMap.put("requestId", requestId);
                        responseMap.put("errorMessage", errMessage);
                        return new ResponseEntity<>(responseMap, HttpStatus.INTERNAL_SERVER_ERROR);
                    }
                }

                tasksMapList = (List<Map>) tasksMap.get("updated");
                if (tasksMapList != null) {
                    List<TaskTree> tasksList = createTaskTreesList(tasksMapList);
                    try {
                        //将assignments updated列表更新到数据库
                        taskTreeService.saveList(tasksList);
                    } catch (Exception ex) {
                        String errMessage = String.format("调用异常：saveTaskTrees(),错误信息：{%s}", ex.toString());
                        LOGGER.error(errMessage);
                        Map responseMap = new HashMap();
                        responseMap.put("success", false);
                        responseMap.put("requestId", requestId);
                        responseMap.put("errorMessage", errMessage);
                        return new ResponseEntity<>(responseMap, HttpStatus.INTERNAL_SERVER_ERROR);
                    }
                }

                tasksMapList = (List<Map>) tasksMap.get("removed");
                if (tasksMapList != null) {
                    List<TaskTree> tasksList = createTaskTreesList(tasksMapList);
                    try {
                        taskTreeService.deleteList(tasksList);
                    } catch (Exception ex) {
                        String errMessage = String.format("调用异常：saveTaskTrees(),错误信息：{%s}", ex.toString());
                        LOGGER.error(errMessage);
                        Map responseMap = new HashMap();
                        responseMap.put("success", false);
                        responseMap.put("requestId", requestId);
                        responseMap.put("errorMessage", errMessage);
                        return new ResponseEntity<>(responseMap, HttpStatus.INTERNAL_SERVER_ERROR);
                    }
                }
            }

            //assignmentsMap = objectMapper.readValue(porridgeMap.get("assignments").toString(), Map.class);
            //assignmentsAdded = objectMapper.readValue(.get("added").toString(),List.class);
            Map responseMap = new HashMap();
            responseMap.put("success", true);
            responseMap.put("requestId", porridgeMap.get("requestId"));
            return new ResponseEntity<>(responseMap, HttpStatus.OK);
        } catch (IOException ex) {
            String errMessage = String.format("调用异常：saveTaskTrees(),错误信息：{%s}", ex.toString());
            LOGGER.error(errMessage);
            Map responseMap = new HashMap();
            responseMap.put("success", false);
            responseMap.put("errorMessage", errMessage);
            return new ResponseEntity<>(responseMap, HttpStatus.INTERNAL_SERVER_ERROR);
        } catch (Exception ex) {
            String errMessage = String.format("调用异常：saveTaskTrees(),错误信息：{%s}", ex.toString());
            LOGGER.error(errMessage);
            Map responseMap = new HashMap();
            responseMap.put("success", false);
            responseMap.put("errorMessage", errMessage);
            return new ResponseEntity<>(responseMap, HttpStatus.INTERNAL_SERVER_ERROR);
        }

    }

    //将 Map List转换成 Assignment List
    private List<Assignment> createAssignmentsList(List<Map> assignmentsMapList) {
        List<Assignment> assignmentsAddedList = new ArrayList();
        if (!assignmentsMapList.isEmpty()) {
            Assignment assignment;
            for (Map assignmentMap : assignmentsMapList) {
                Object idObj = assignmentMap.get("Id");
                if (idObj != null) {
                    Long id = Long.parseLong(idObj.toString());
                    try {
                        assignment = assignmentService.getAssignmentById(id);
                    } catch (Exception ex) {
                        LOGGER.error(String.format("调用异常：createAssignmentsList(),错误信息：{%s}", ex.toString()));
                        return null;
                    }
                } else {
                    assignment = new Assignment();
                }

                Object resourceIdObj = assignmentMap.get("ResourceId");
                if (resourceIdObj != null) {
                    assignment.setResourceId(Long.parseLong(resourceIdObj.toString()));
                }

                Object taskIdObj = assignmentMap.get("TaskId");
                if (taskIdObj != null) {
                    assignment.setTaskId(Long.parseLong(taskIdObj.toString()));
                }

                Object unitsObj = assignmentMap.get("Units");
                if (unitsObj != null) {
                    assignment.setUnits(Double.parseDouble(unitsObj.toString()));
                }

                assignmentsAddedList.add(assignment);
            }
        }
        return assignmentsAddedList;
    }

    //将 Map List转换成 Dependency List
    private List<Dependency> createDependenciesList(List<Map> dependenciesMapList) {
        List<Dependency> dependenciesList = new ArrayList();
        if (!dependenciesMapList.isEmpty()) {
            Dependency dependency;
            for (Map dependencyMap : dependenciesMapList) {
                Object idObj = dependencyMap.get("Id");
                if (idObj != null) {
                    Long id = Long.parseLong(idObj.toString());
                    try {
                        dependency = dependencyService.getOne(id);
                    } catch (Exception ex) {
                        LOGGER.error(String.format("调用异常：createDependenciesList(),错误信息：{%s}", ex.toString()));
                        return null;
                    }
                } else {
                    dependency = new Dependency();
                }

                Object fromObj = dependencyMap.get("From");
                if (fromObj != null) {
                    dependency.setFrom(Long.parseLong(fromObj.toString()));
                }

                Object toObj = dependencyMap.get("To");
                if (toObj != null) {
                    dependency.setTo(Long.parseLong(toObj.toString()));
                }

                Object typeObj = dependencyMap.get("Type");
                if (typeObj != null) {
                    dependency.setType(Integer.parseInt(typeObj.toString()));
                }

                dependenciesList.add(dependency);
            }
        }
        return dependenciesList;
    }

    //将 Map List转换成 TaskTree List
    private List<TaskTree> createTaskTreesList(List<Map> taskTreesMapList) throws Exception {
        List<TaskTree> taskTreesList = new ArrayList();
        if (!taskTreesMapList.isEmpty()) {
            TaskTree taskTree;
            for (Map taskTreesMap : taskTreesMapList) {
                Object idObj = taskTreesMap.get("Id");
                if (idObj != null) {
                    Long id = Long.parseLong(idObj.toString());
//                    try {
                    taskTree = taskTreeService.getOne(id);
//                    } catch (Exception ex) {
//                        LOGGER.error(String.format("调用异常：createTaskTreesList(),错误信息：{%s}", ex.toString()));
//                        return null;
//                    }
                } else {
                    taskTree = new TaskTree();
                }

                Object parentIdObj = taskTreesMap.get("parentId");
                if ((parentIdObj != null) && (parentIdObj instanceof Integer)) {
                    taskTree.setParentId(Long.parseLong(parentIdObj.toString()));
                }

                Object nameObj = taskTreesMap.get("Name");
                if (nameObj != null) {
                    taskTree.setName((String) nameObj);
                }

                Object percentDoneObj = taskTreesMap.get("PercentDone");
                if (percentDoneObj != null) {
                    taskTree.setPercentDone(Double.parseDouble(percentDoneObj.toString()));
                }

                Object startDateObj = taskTreesMap.get("StartDate");
                if (startDateObj != null) {
                    SimpleDateFormat dfSource = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ssX");
//                    try {
                    taskTree.setStartDate(new Timestamp(dfSource.parse(startDateObj.toString()).getTime()));
//                    } catch (ParseException ex) {
//                        LOGGER.error(String.format("调用异常：createTaskTreesList(),错误信息：{%s}", ex.toString()));
//                        return null;
//                    }
                }

                Object endDateObj = taskTreesMap.get("EndDate");
                if (endDateObj != null) {
                    SimpleDateFormat dfSource = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ssX");
//                    try {
                    taskTree.setEndDate(new Timestamp(dfSource.parse(endDateObj.toString()).getTime()));
//                    } catch (ParseException ex) {
//                        LOGGER.error(String.format("调用异常：createTaskTreesList(),错误信息：{%s}", ex.toString()));
//                        return null;
//                    }
                }

                Object baselineStartDateObj = taskTreesMap.get("BaselineStartDate");
                if (baselineStartDateObj != null) {
                    SimpleDateFormat dfSource = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ssX");
//                    try {
                    taskTree.setBaselineStartDate(new Timestamp(dfSource.parse(baselineStartDateObj.toString()).getTime()));
//                    } catch (ParseException ex) {
//                        LOGGER.error(String.format("调用异常：createTaskTreesList(),错误信息：{%s}", ex.toString()));
//                        return null;
//                    }
                }

                Object baselineEndDateObj = taskTreesMap.get("BaselineEndDate");
                if (baselineEndDateObj != null) {
                    SimpleDateFormat dfSource = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ssX");
//                    try {
                    taskTree.setBaselineEndDate(new Timestamp(dfSource.parse(baselineEndDateObj.toString()).getTime()));
//                    } catch (ParseException ex) {
//                        LOGGER.error(String.format("调用异常：createTaskTreesList(),错误信息：{%s}", ex.toString()));
//                        return null;
//                    }
                }

                Object leafObj = taskTreesMap.get("leaf");
                if (leafObj != null) {
                    taskTree.setLeaf((Boolean) leafObj);
                }

                Object durationObj = taskTreesMap.get("Duration");
                if (durationObj != null) {
                    taskTree.setDuration(Double.parseDouble(durationObj.toString()));
                }

                Object resizableObj = taskTreesMap.get("Resizable");
                if (resizableObj != null) {
                    taskTree.setResizable((Boolean) resizableObj);
                }

                Object draggableObj = taskTreesMap.get("Draggable");
                if (draggableObj != null) {
                    taskTree.setDraggable((Boolean) draggableObj);
                }

                Object manuallyScheduledObj = taskTreesMap.get("ManuallyScheduled");
                if (manuallyScheduledObj != null) {
                    taskTree.setManuallyScheduled((Boolean) manuallyScheduledObj);
                }

                Object expandedObj = taskTreesMap.get("expanded");
                if (expandedObj != null) {
                    taskTree.setExpanded((Boolean) expandedObj);
                }

                Object indexObj = taskTreesMap.get("index");
                if (indexObj != null) {
                    taskTree.setIndex(Integer.parseInt(indexObj.toString()));
                }

                taskTreesList.add(taskTree);
            }
        }
        return taskTreesList;
    }

    /**
     * 员工检索
     *
     * @param AppointDate
     * @param empUserId
     * @param isFinish
     * @return
     */
    @RequestMapping(value = "findOpenUserOrById/{AppointDate}", method = RequestMethod.GET)
    public Object findOpenUserOrById( @PathVariable("AppointDate") Timestamp AppointDate, Long empUserId, String isFinish) {
        Map<String, String> map = new HashMap<>();
        if (AppointDate==null){
            map.put("error","请输入时间");
            return  map;
        }
        List<TaskTree> taskTrees = null;
        try {
            taskTrees = taskTreeService.findOpenUserOrById(AppointDate, empUserId, isFinish);
            if (taskTrees.size()==0){
                map.put("error","无符合条件信息");
                return  map;
            }
        } catch (Exception e) {
            map.put("error","服务器错误");
            return  map;
        }
        return taskTrees;
    }
}
