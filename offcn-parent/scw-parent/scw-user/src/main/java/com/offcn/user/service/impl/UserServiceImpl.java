package com.offcn.user.service.impl;

import com.offcn.user.enums.UserExceptionEnum;
import com.offcn.user.exception.UserException;
import com.offcn.user.mapper.TMemberAddressMapper;
import com.offcn.user.mapper.TMemberMapper;
import com.offcn.user.pojo.TMember;
import com.offcn.user.pojo.TMemberAddress;
import com.offcn.user.pojo.TMemberAddressExample;
import com.offcn.user.pojo.TMemberExample;
import com.offcn.user.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import java.util.List;

/**
 * @Auther: lhq
 * @Date: 2020/9/6 15:42
 * @Description:
 */
@Service
public class UserServiceImpl implements UserService {

    @Autowired
    private TMemberMapper memberMapper;


    @Autowired
    private TMemberAddressMapper memberAddressMapper;

    /**
     * 注册会员
     *
     * @param tMember
     */
    @Override
    public void register(TMember tMember) {
        //1.判断手机号是否存在
        TMemberExample memberExample = new TMemberExample();
        TMemberExample.Criteria criteria = memberExample.createCriteria();
        criteria.andLoginacctEqualTo(tMember.getLoginacct());
        long l = memberMapper.countByExample(memberExample);    //判断记录数
        if (l > 0) {
            throw new UserException(UserExceptionEnum.LOGINACCT_EXIST);
        }
        //2.密码做加密操作
        BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();
        String password = encoder.encode(tMember.getUserpswd());
        tMember.setUserpswd(password);

        //3.设置状态信息
        tMember.setAuthstatus("0");  //实名认证状态 0 - 未实名认证， 1 - 实名认证申请中， 2 - 已实名认证
        tMember.setUsertype("0");// 用户类型: 0 - 个人， 1 - 企业
        tMember.setAccttype("2");//账户类型: 0 - 企业， 1 - 个体， 2 - 个人， 3 - 政府
        tMember.setUsername(tMember.getLoginacct());
        //执行保存
        memberMapper.insertSelective(tMember);
    }

    /**
     * 用户登录
     *
     * @param username
     * @param password
     * @return
     */
    @Override
    public TMember login(String username, String password) {
        BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();
        TMemberExample memberExample = new TMemberExample();
        TMemberExample.Criteria criteria = memberExample.createCriteria();
        criteria.andLoginacctEqualTo(username);
        List<TMember> memberList = memberMapper.selectByExample(memberExample);

        if (!CollectionUtils.isEmpty(memberList)) {
            TMember member = memberList.get(0);
            //通过加密方式比对密码是否一致
            boolean matches = encoder.matches(password, member.getUserpswd());
            return matches ? member : null;
        }
        return null;
    }

    /**
     * 通过ID查询会员信息
     *
     * @param id
     * @return
     */
    @Override
    public TMember findTMemberById(Integer id) {
        return memberMapper.selectByPrimaryKey(id);
    }

    /**
     * 通过当前登录会员的ID 查询地址列表
     *
     * @param memberId
     * @return
     */
    @Override
    public List<TMemberAddress> findAddressList(Integer memberId) {
        TMemberAddressExample memberAddressExample = new TMemberAddressExample();
        TMemberAddressExample.Criteria criteria = memberAddressExample.createCriteria();
        criteria.andMemberidEqualTo(memberId);
        return memberAddressMapper.selectByExample(memberAddressExample);
    }
}
