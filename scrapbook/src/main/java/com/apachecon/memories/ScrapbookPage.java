/**
 * Licensed to the Apache Software Foundation (ASF) under one or more
 * contributor license agreements.  See the NOTICE file distributed with
 * this work for additional information regarding copyright ownership.
 * The ASF licenses this file to You under the Apache License, Version 2.0
 * (the "License"); you may not use this file except in compliance with
 * the License.  You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.apachecon.memories;

import com.apachecon.memories.session.Logout;
import com.apachecon.memories.session.SignIn;

import java.util.ArrayList;
import java.util.List;

import org.apache.wicket.AttributeModifier;
import org.apache.wicket.Page;
import org.apache.wicket.authroles.authentication.AuthenticatedWebSession;
import org.apache.wicket.authroles.authorization.strategies.role.Roles;
import org.apache.wicket.markup.html.WebPage;
import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.markup.html.link.BookmarkablePageLink;
import org.apache.wicket.markup.html.link.ExternalLink;
import org.apache.wicket.markup.html.list.ListItem;
import org.apache.wicket.markup.html.list.ListView;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class ScrapbookPage extends WebPage {

    private static final long serialVersionUID = 7287222304337988722L;

    protected final transient Logger logger = LoggerFactory.getLogger(getClass());

    @SuppressWarnings({"rawtypes", "unchecked"})
    public ScrapbookPage() {
        add(new ExternalLink("apacheCon", "http://na11.apachecon.com/"));

        add(new BookmarkablePageLink("logo", Index.class));

        List<Class<? extends Page>> links = new ArrayList<Class<? extends Page>>();
        links.add(Index.class);
        links.add(Upload.class);
        Roles roles = AuthenticatedWebSession.get().getRoles();
        if (roles != null && roles.hasRole("admin")) {
            links.add(Approve.class);
            links.add(Logout.class);
        } else {
            links.add(Browse.class);
            links.add(SignIn.class);
        }

        add(new ListView<Class>("menu", links) {
			private static final long serialVersionUID = 1L;

			@Override
            protected void populateItem(ListItem<Class> item) {
                BookmarkablePageLink link = new BookmarkablePageLink("link", item.getModelObject());

                String simpleName = item.getModelObject().getSimpleName();

                if (getPage().getClass().equals(item.getModelObject())) {
                    item.add(AttributeModifier.append("class", "active"));
                }

                link.add(new Label("label", simpleName));
                item.add(link);
            }
        });
    }

}
