package com.atguigu.mybatis_plus_180925;


import com.atguigu.mybatis_plus_180925.entity.User;
import com.atguigu.mybatis_plus_180925.mapper.UserMapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import com.baomidou.mybatisplus.core.toolkit.IdWorker;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;

@RunWith(SpringRunner.class)
@SpringBootTest
public class CRUDTest {
    @Autowired
    private UserMapper userMapper;

    @Test
    public void testInsert(){
        User user = new User();
        user.setName("xiaofu");
        user.setAge(9);
        user.setEmail("123456@");

        int insert = userMapper.insert(user);
        System.out.println(insert);
    }
 /*  @Test
    public void testIdWorker(){
        long id = new IdWorker().nextId();
        System.out.println(id);

    }*/
    @Test
    public void testUpdateById(){

        User user = new User();
        user.setId(1L);
        user.setAge(28);

        int result = userMapper.updateById(user);
        System.out.println(result);

    }
    @Test
    public void testOptimisticLocker(){
        //查询
        User user = userMapper.selectById(2);
        //修改
        user.setName("xiaoxiaochuxian");
        user.setAge(20);

        userMapper.updateById(user);
    }
    //批量查询
    @Test
    public void testSelectById(){
        List<User> users = userMapper.selectBatchIds(Arrays.asList(1, 2, 3));
        users.forEach(System.out::println);
    }
    //条件查询
    @Test
    public void testSelectByMap(){
        HashMap<String,Object> map =new HashMap();
        map.put("name","daxiong");
        map.put("age",20);
        List<User> users = userMapper.selectByMap(map);

        users.forEach(System.out::println);
    }

    @Test
    public void testSelectPage(){
        Page<User> page =new Page();
        userMapper.selectPage(page,null);

        page.getRecords().forEach(System.out::println);
        System.out.println(page.getSize());
        System.out.println(page.getTotal());

    }
    /**
     * 测试 逻辑删除
     */
    @Test
    public void testLogicDelete() {

        int result = userMapper.deleteById(1L);
        System.out.println(result);
    }

    @Test
    public void testPerformance() {

        User user = new User();
        user.setId(11L);
        user.setName("我是Helen");
        user.setEmail("helen@sina.com");
        user.setAge(18);
        userMapper.insert(user);
    }

    @Test
    public void testDelete() {

        QueryWrapper<User> queryWrapper = new QueryWrapper<>();
        queryWrapper
                .isNull("name")
                .ge("age", 12)
                .isNotNull("email");
        int result = userMapper.delete(queryWrapper);
        System.out.println("delete return count = " + result);
    }

    //条件构造器
    /**
     * Wrapper ： 条件构造抽象类，最顶端父类
     AbstractWrapper ： 用于查询条件封装，生成 sql 的 where 条件
     QueryWrapper ： Entity 对象封装操作类，不是用lambda语法
     UpdateWrapper ： Update 条件封装，用于Entity对象更新操作
     AbstractLambdaWrapper ： Lambda 语法使用 Wrapper统一处理解析 lambda 获取 column。
     LambdaQueryWrapper ：看名称也能明白就是用于Lambda语法使用的查询Wrapper
     LambdaUpdateWrapper ： Lambda 更新封装Wrapper

     */

    /**
     * 1.ge、gt、le、lt、isNull、isNotNull
     SQL：UPDATE user SET deleted=1 WHERE deleted=0 AND name IS NULL AND age >= ? AND email IS NOT NULL
     */
    //2.eq ne
    //注意：seletOne返回的是一条实体记录，当出现多条时会报错

    @Test
    public void testSelectOne() {

        QueryWrapper<User> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("name", "Tom");

        User user = userMapper.selectOne(queryWrapper);
        System.out.println(user);
    }
    /**
     * 6、in、notIn、inSql、notinSql、exists、notExists
     in、notIn：
     notIn("age",{1,2,3})--->age not in (1,2,3)
     notIn("age", 1, 2, 3)--->age not in (1,2,3)
     inSql、notinSql：可以实现子查询
     例: inSql("age", "1,2,3,4,5,6")--->age in (1,2,3,4,5,6)
     例: inSql("id", "select id from table where id < 3")--->id in (select id from table where id < 3)
     */

    @Test
    public void testSelectObjs() {

        QueryWrapper<User> queryWrapper = new QueryWrapper<>();
        //queryWrapper.in("id", 1, 2, 3);
        queryWrapper.inSql("id", "select id from user where id < 3");

        List<Object> objects = userMapper.selectObjs(queryWrapper);//返回值是Object列表
        objects.forEach(System.out::println);
    }
    /**
    * 8、嵌套or、嵌套and
     这里使用了lambda表达式，or中的表达式最后翻译成sql时会被加上圆括号
    */
    @Test
    public void testUpdate2() {


        //修改值
        User user = new User();
        user.setAge(99);
        user.setName("Andy");

        //修改条件
        UpdateWrapper<User> userUpdateWrapper = new UpdateWrapper<>();
        userUpdateWrapper
                .like("name", "h")
                .or(i -> i.eq("name", "李白").ne("age", 20));

        int result = userMapper.update(user, userUpdateWrapper);

        System.out.println(result);
    }
    //10、last
    //直接拼接到 sql 的最后
    //注意：只能调用一次,多次调用以最后一次为准 有sql注入的风险,请谨慎使用
    @Test
    public void testSelectListLast() {

        QueryWrapper<User> queryWrapper = new QueryWrapper<>();
        queryWrapper.last("limit 1");

        List<User> users = userMapper.selectList(queryWrapper);
        users.forEach(System.out::println);
    }
    /**
     * 12、set、setSql
     最终的sql会合并 user.setAge()，以及 userUpdateWrapper.set()  和 setSql() 中 的字段
     */
    @Test
    public void testUpdateSet() {

        //修改值
        User user = new User();
        user.setAge(99);

        //修改条件
        UpdateWrapper<User> userUpdateWrapper = new UpdateWrapper<>();
        userUpdateWrapper
                .like("name", "h")
                .set("name", "老李头")//除了可以查询还可以使用set设置修改的字段
                .setSql(" email = '123@qq.com'");//可以有子查询

        int result = userMapper.update(user, userUpdateWrapper);
    }
}
