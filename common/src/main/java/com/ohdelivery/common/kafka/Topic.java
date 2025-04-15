package com.ohdelivery.common.kafka;

public class Topic {

  public static final String CREATE_DELIVERY = "create-delivery";
  public static final String COMPLETE_DELIVERY = "delivery-record-create";
  public static final String UPDATE_DELIVERY = "update-delivery";

  public static final String COMPLETE_MATCHING = "complete-matching";

  public static final String UPDATED_SLACK_ID = "update-slackId";
  public static final String DELETED_USER = "delete-user";
}
