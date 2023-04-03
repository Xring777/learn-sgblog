package com.liuzihan.framework.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.liuzihan.framework.domain.ResponseResult;
import com.liuzihan.framework.domain.vo.PageVo;
import com.liuzihan.framework.domain.vo.UserInfoVo;
import com.liuzihan.framework.enums.AppHttpCodeEnum;
import com.liuzihan.framework.exception.SystemException;
import com.liuzihan.framework.mapper.UserMapper;
import com.liuzihan.framework.domain.entity.User;
import com.liuzihan.framework.service.UserService;
import com.liuzihan.framework.utils.BeanCopyUtils;
import com.liuzihan.framework.utils.SecurityUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.util.List;
import java.util.Objects;

/**
 * 用户表(User)表服务实现类
 *
 * @author liuzihan
 * @since 2022-03-31 21:41:31
 */
@Service
public class UserServiceImpl extends ServiceImpl<UserMapper, User> implements UserService {
    @Autowired
    private PasswordEncoder passwordEncoder;

    @Override
    public ResponseResult userInfo() {
        // 从token中获取获取 userId 并查询用户信息
        User user = getById(SecurityUtils.getUserId());

        // 封装为 userInfoVo
        UserInfoVo userInfoVo = BeanCopyUtils.copyBean(user, UserInfoVo.class);
        return ResponseResult.okResult(userInfoVo);
    }

    @Override
    public ResponseResult updateUserInfo(User user) {
        //对数据进行非空判断
        if(!StringUtils.hasText(user.getEmail())){
            throw new SystemException(AppHttpCodeEnum.EMAIL_NOT_NULL);
        }
        if(!StringUtils.hasText(user.getNickName())){
            throw new SystemException(AppHttpCodeEnum.NICKNAME_NOT_NULL);
        }
        //对数据进行是否存在的判断
        if(nickNameExist(user)){
            throw new SystemException(AppHttpCodeEnum.NICKNAME_EXIST);
        }
        if(emailExist(user)){
            throw new SystemException(AppHttpCodeEnum.EMAIL_EXIST);
        }
        updateById(user);
        return ResponseResult.okResult();
    }

    @Override
    public ResponseResult register(User user) {
        //对数据进行非空判断
        if(!StringUtils.hasText(user.getUserName())){
            throw new SystemException(AppHttpCodeEnum.USERNAME_NOT_NULL);
        }
        if(!StringUtils.hasText(user.getPassword())){
            throw new SystemException(AppHttpCodeEnum.PASSWORD_NOT_NULL);
        }
        if(!StringUtils.hasText(user.getEmail())){
            throw new SystemException(AppHttpCodeEnum.EMAIL_NOT_NULL);
        }
        if(!StringUtils.hasText(user.getNickName())){
            throw new SystemException(AppHttpCodeEnum.NICKNAME_NOT_NULL);
        }
        //对数据进行是否存在的判断
        if(userNameExist(user)){
            throw new SystemException(AppHttpCodeEnum.USERNAME_EXIST);
        }
        if(nickNameExist(user)){
            throw new SystemException(AppHttpCodeEnum.NICKNAME_EXIST);
        }
        if(emailExist(user)){
            throw new SystemException(AppHttpCodeEnum.EMAIL_EXIST);
        }
        //对密码进行加密
        String encodePassword = passwordEncoder.encode(user.getPassword());
        user.setPassword(encodePassword);
        //存入数据库
        save(user);
        return ResponseResult.okResult();
    }

    @Override
    public ResponseResult updateUserStatus(User user) {
        updateById(user);
        return ResponseResult.okResult();
    }

    @Override
    public ResponseResult selectUser(User user, Integer pageNum, Integer pageSize) {
        LambdaQueryWrapper<User> queryWrapper = new LambdaQueryWrapper<>();
        if (Objects.nonNull(user)) {
            queryWrapper.eq(Objects.nonNull(user.getId()), User::getId,user.getId());
            queryWrapper.eq(Objects.nonNull(user.getStatus()), User::getStatus, user.getStatus());
            queryWrapper.eq(Objects.nonNull(user.getType()), User::getType, user.getType());
            queryWrapper.eq(Objects.nonNull(user.getSex()), User::getSex, user.getSex());
            queryWrapper.like(StringUtils.hasText(user.getNickName()), User::getNickName, user.getNickName());
            queryWrapper.like(StringUtils.hasText(user.getPhonenumber()), User::getPhonenumber, user.getPhonenumber());
            queryWrapper.like(StringUtils.hasText(user.getEmail()), User::getEmail, user.getEmail());
            queryWrapper.like(StringUtils.hasText(user.getUserName()), User::getUserName, user.getUserName());
            queryWrapper.ge(Objects.nonNull(user.getCreateTime()),User::getCreateTime,user.getCreateTime());
        }
        Page<User> page = new Page<>(pageNum,pageSize);
        page(page,queryWrapper);
        List<UserInfoVo> userInfoVos = BeanCopyUtils.copyBeanList(page.getRecords(), UserInfoVo.class);
        PageVo pageVo = new PageVo(userInfoVos, page.getTotal());
        return ResponseResult.okResult(pageVo);
    }

    @Override
    public ResponseResult deleteUser(Long id) {
        removeById(id);
        return ResponseResult.okResult();
    }

    private boolean emailExist(User user) {
        LambdaQueryWrapper<User> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(User::getEmail,user.getEmail());
        wrapper.ne(user.getId() != null, User::getId, user.getId());
        return getOne(wrapper) != null;
    }

    private boolean nickNameExist(User user) {
        LambdaQueryWrapper<User> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(User::getNickName,user.getNickName());
        wrapper.ne(user.getId() != null, User::getId, user.getId());
        return getOne(wrapper) != null;
    }

    private boolean userNameExist(User user) {
        LambdaQueryWrapper<User> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(User::getUserName,user.getUserName());
        wrapper.ne(user.getId() != null, User::getId, user.getId());
        return getOne(wrapper) != null;
    }
}

