package com.offcn.user.service;

import com.offcn.user.pojo.TMember;
import com.offcn.user.pojo.TMemberAddress;

import java.util.List;

/**
 * @Auther: lhq
 * @Date: 2020/9/6 15:41
 * @Description: 用户模块接口
 */
public interface UserService {

    /**
     * 注册会员
     * @param tMember
     */
    public void register(TMember tMember);


    /**
     * 用户登录
     * @param username
     * @param password
     * @return
     */
    public TMember login(String username,String password);

    /**
     * 通过ID查询会员信息
     * @param id
     * @return
     */
    public TMember findTMemberById(Integer id);


    /**
     * 通过当前登录会员的ID 查询地址列表
     * @param memberId
     * @return
     */
    public List<TMemberAddress> findAddressList(Integer memberId);
}
