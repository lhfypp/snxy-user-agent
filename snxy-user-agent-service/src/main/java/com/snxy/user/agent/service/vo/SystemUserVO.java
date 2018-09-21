package com.snxy.user.agent.service.vo;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;


/**
 * @author Administrator
 * @date 2018-08-29
 */
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class SystemUserVO {
    private String token;
    private String name;

  //  private Long systemUserId;
  //  private List<UserIdentity> identityTypes;

}