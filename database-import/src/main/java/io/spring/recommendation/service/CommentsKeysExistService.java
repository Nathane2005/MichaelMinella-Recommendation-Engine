/*
 * Copyright 2014 the original author or authors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package io.spring.recommendation.service;

import io.spring.recommendation.domain.Comment;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcOperations;

/**
 * @author Michael Minella
 */
public class CommentsKeysExistService extends AbstractCachingExistsService<Comment> {

	@Autowired
	private JdbcOperations jdbcTemplate;

	@Override
	public boolean exists(Comment comment) {
		String postId = String.valueOf(comment.getPostId());
		String userId = String.valueOf(comment.getUserId());
		String cacheKey = postId + ":" + userId;

		if(!isCached(cacheKey)) {
			Long postCount = jdbcTemplate.queryForObject("select count(*) from post where id = ?", Long.class, comment.getPostId());
			Long userCount = jdbcTemplate.queryForObject("select count(*) from users where id = ?", Long.class, comment.getUserId());

			if(postCount != null && postCount > 0 && userCount != null && userCount > 0) {
				cache(cacheKey);
				return true;
			} else {
				return false;
			}
		} else {
			return true;
		}
	}
}
