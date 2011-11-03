package com.apachecon.memories;

import com.apachecon.memories.service.ApprovalService;
import com.apachecon.memories.service.DefaultImageService;
import com.apachecon.memories.service.ImageService;
import com.apachecon.memories.session.Logout;
import com.apachecon.memories.session.MemoriesWebSession;
import com.apachecon.memories.session.SignIn;

import java.io.File;
import java.io.IOException;
import java.util.Properties;

import org.apache.wicket.authroles.authentication.AbstractAuthenticatedWebSession;
import org.apache.wicket.authroles.authentication.AuthenticatedWebApplication;
import org.apache.wicket.markup.html.WebPage;

/**
 * Application object for your web application. If you want to run this
 * application without deploying, run the Start class.
 * 
 * @see com.apachecon.memories.Start#main(String[])
 */
public class ScrapbookApplication extends AuthenticatedWebApplication {

    private DefaultImageService imageService;

    /**
     * @see org.apache.wicket.Application#getHomePage()
     */
    @Override
    public Class<Index> getHomePage() {
        return Index.class;
    }

    /**
     * @see org.apache.wicket.Application#init()
     */
    @Override
    public void init() {
        mountPage("/index", Index.class);
        mountPackage("/signin", SignIn.class);
        mountPackage("/logout", Logout.class);
        mountPackage("/aprove", Approve.class);
        mountPackage("/upload", Upload.class);
        super.init();

        Properties props = new Properties();
        try {
            props.load(getClass().getResourceAsStream("/deploy.properties"));
        } catch (IOException e) {
            
        }
        imageService = new DefaultImageService();
        imageService.setUploadDirectory(new File(props.getProperty("upload")));
        imageService.setAproveDirectory(new File(props.getProperty("approve")));
        imageService.setDeclineDirectory(new File(props.getProperty("decline")));
    }

    @Override
    protected Class<? extends WebPage> getSignInPageClass() {
        return SignIn.class;
    }

    @Override
    protected Class<? extends AbstractAuthenticatedWebSession> getWebSessionClass() {
        return MemoriesWebSession.class;
    }

    public ImageService getImageService() {
        return imageService;
    }

    public ApprovalService getApprovalService() {
        return null;
    }
}
