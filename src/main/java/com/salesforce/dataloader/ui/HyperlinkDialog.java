/*
 * Copyright (c) 2015, salesforce.com, inc.
 * All rights reserved.
 *
 * Redistribution and use in source and binary forms, with or without modification, are permitted provided
 * that the following conditions are met:
 *
 *    Redistributions of source code must retain the above copyright notice, this list of conditions and the
 *    following disclaimer.
 *
 *    Redistributions in binary form must reproduce the above copyright notice, this list of conditions and
 *    the following disclaimer in the documentation and/or other materials provided with the distribution.
 *
 *    Neither the name of salesforce.com, inc. nor the names of its contributors may be used to endorse or
 *    promote products derived from this software without specific prior written permission.
 *
 * THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS "AS IS" AND ANY EXPRESS OR IMPLIED
 * WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE IMPLIED WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A
 * PARTICULAR PURPOSE ARE DISCLAIMED. IN NO EVENT SHALL THE COPYRIGHT OWNER OR CONTRIBUTORS BE LIABLE FOR
 * ANY DIRECT, INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, OR CONSEQUENTIAL DAMAGES (INCLUDING, BUT NOT LIMITED
 * TO, PROCUREMENT OF SUBSTITUTE GOODS OR SERVICES; LOSS OF USE, DATA, OR PROFITS; OR BUSINESS INTERRUPTION)
 * HOWEVER CAUSED AND ON ANY THEORY OF LIABILITY, WHETHER IN CONTRACT, STRICT LIABILITY, OR TORT (INCLUDING
 * NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY OUT OF THE USE OF THIS SOFTWARE, EVEN IF ADVISED OF THE
 * POSSIBILITY OF SUCH DAMAGE.
 */

package com.salesforce.dataloader.ui;

import java.awt.Desktop;
import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.ArrayList;

import org.eclipse.jface.dialogs.IDialogConstants;
import org.eclipse.jface.resource.JFaceColors;
import org.eclipse.jface.resource.JFaceResources;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.*;
import org.eclipse.swt.graphics.Color;
import org.eclipse.swt.layout.*;
import org.eclipse.swt.widgets.*;

import com.salesforce.dataloader.controller.Controller;
import com.salesforce.dataloader.util.AppUtil;

public class HyperlinkDialog extends BaseDialog {
    private String boldMessage;
    private String linkText;
    private String linkURL;

    public String getLinkURL() {
        return linkURL;
    }

    public void setLinkURL(String linkURL) {
        this.linkURL = linkURL;
    }

    private Text messageLabel;
    private Label titleLabel;
    private Label titleImage;
    private Label titleBanner;
    private Hyperlink link;

    /**
     * InputDialog constructor
     *
     * @param parent
     *            the parent
     */
    public HyperlinkDialog(Shell parent, Controller controller) {
        super(parent, controller);
    }

    /**
     * Opens the dialog and returns the input
     *
     * @return String
     */
    public boolean open() {
        // Create the dialog window
        Shell shell = super.openAndGetShell();
        Display display = shell.getDisplay();

        while (!shell.isDisposed()) {
            if (!display.readAndDispatch()) {
                display.sleep();
            }
        }
        // Return the sucess
        return true;
    }

    /**
     * Creates the dialog's contents
     *
     * @param shell
     *            the dialog window
     */
    protected void createContents(final Shell shell) {

        FormLayout layout = new FormLayout();
        shell.setLayout(layout);
        FormData data = new FormData();
        data.top = new FormAttachment(0, 0);
        data.bottom = new FormAttachment(100, 0);
        shell.setLayoutData(data);

        Color background = JFaceColors.getBannerBackground(shell.getDisplay());
        Color foreground = JFaceColors.getBannerForeground(shell.getDisplay());
        shell.setBackground(background);

        titleImage = new Label(shell, SWT.CENTER);
        titleImage.setBackground(background);
        titleImage.setImage(UIUtils.getImageRegistry().get("splashscreens"));
        FormData imageData = new FormData();
        imageData.top = new FormAttachment(0, 10);
        imageData.right = new FormAttachment(100, 0); // horizontalSpacing
        titleImage.setLayoutData(imageData);

        titleBanner = new Label(shell, SWT.LEFT);
        titleBanner.setBackground(background);
        titleBanner.setImage(UIUtils.getImageRegistry().get("logo"));
        FormData bannerData = new FormData();
        bannerData.top = new FormAttachment(0, 10);
        bannerData.right = new FormAttachment(titleImage);
        bannerData.left = new FormAttachment(0, 10);
        titleBanner.setLayoutData(bannerData);

        titleLabel = new Label(shell, SWT.LEFT);
        titleLabel.setForeground(foreground);
        titleLabel.setBackground(background);
        titleLabel.setFont(JFaceResources.getBannerFont());
        titleLabel.setText(boldMessage);
        FormData titleData = new FormData();
        titleData.top = new FormAttachment(titleBanner, 10);
        titleData.right = new FormAttachment(titleImage);
        titleData.left = new FormAttachment(0, 10);
        titleLabel.setLayoutData(titleData);

        messageLabel = new Text(shell, SWT.WRAP | SWT.READ_ONLY);
        messageLabel.setForeground(foreground);
        messageLabel.setBackground(background);
        messageLabel.setText(this.getMessage()); // two lines
        messageLabel.setFont(JFaceResources.getDialogFont());
        FormData messageLabelData = new FormData();
        messageLabelData.top = new FormAttachment(titleLabel, 10);
        messageLabelData.right = new FormAttachment(titleImage);
        messageLabelData.left = new FormAttachment(0, 10);
        //        messageLabelData.bottom = new FormAttachment(titleImage, 0, SWT.BOTTOM);
        messageLabel.setLayoutData(messageLabelData);

        link = new Hyperlink(shell, SWT.NONE);
        link.setText(getLinkText());
        link.setBackground(background);
        link.addSelectionListener(new SelectionListener() {
            @Override
            public void widgetSelected(SelectionEvent e) {
                Thread runner = new Thread() {
                    @Override
                    public void run() {
                        int exitVal = 0;
                        if (System.getProperty("os.name").toLowerCase().indexOf("windows") >= 0) {
                            ArrayList<String> cmd = new ArrayList<String>();
                            cmd.add("rundll32");
                            cmd.add("url.dll,");
                            cmd.add("FileProtocolHandler");
                            cmd.add(getLinkURL());
                            AppUtil.exec(cmd, "Browser Error");
                        } else if (System.getProperty("os.name").toLowerCase().indexOf("mac") >= 0){
                            Desktop desktop = Desktop.getDesktop();
                            try {
                                desktop.browse(new URI(getLinkURL()));
                            } catch (URISyntaxException | IOException e) {
                                // TODO Auto-generated catch block
                                logger.error("Browser Error");
                            }
                        }
                        else {
                            logger.error("OS is not supported.");
                            return;
                        }

                        if (exitVal != 0) {
                            logger.error("Process exited with error" + exitVal);
                        }
                    }
                };

                runner.setPriority(Thread.MAX_PRIORITY);
                runner.start();
            }

            @Override
            public void widgetDefaultSelected(SelectionEvent e) {}
        });
        FormData linkData = new FormData();
        linkData.top = new FormAttachment(messageLabel);
        linkData.right = new FormAttachment(titleImage);
        linkData.left = new FormAttachment(0, 10);
        link.setLayoutData(linkData);

        Composite greyArea = new Composite(shell, SWT.NULL);
        GridLayout childLayout = new GridLayout(1, false);
        childLayout.marginHeight = 0;
        childLayout.marginWidth = 0;
        childLayout.verticalSpacing = 0;
        childLayout.horizontalSpacing = 0;
        greyArea.setLayout(childLayout);

        FormData childData = new FormData();
        childData.top = new FormAttachment(link, 5);
        childData.right = new FormAttachment(100, 0);
        childData.left = new FormAttachment(0, 0);
        childData.bottom = new FormAttachment(100, 0);
        greyArea.setLayoutData(childData);

        Label titleBarSeparator = new Label(greyArea, SWT.HORIZONTAL | SWT.SEPARATOR);
        titleBarSeparator.setLayoutData(new GridData(GridData.FILL_HORIZONTAL));

        Composite compositeX = new Composite(greyArea, SWT.NONE);
        GridLayout layoutComp = new GridLayout();
        layoutComp.marginWidth = 10;
        layoutComp.marginHeight = 10;
        layoutComp.horizontalSpacing = 10;
        layoutComp.verticalSpacing = 10;
        compositeX.setLayout(layoutComp);

        GridData dataComp = new GridData(GridData.HORIZONTAL_ALIGN_END | GridData.VERTICAL_ALIGN_CENTER);
        compositeX.setLayoutData(dataComp);

        Button ok = new Button(compositeX, SWT.PUSH | SWT.FLAT);
        ok.setText(IDialogConstants.OK_LABEL);
        GridData okData = new GridData();
        okData.widthHint = Math.max(75, ok.computeSize(SWT.DEFAULT, SWT.DEFAULT, true).x);
        ok.setLayoutData(okData);
        ok.addSelectionListener(new SelectionAdapter() {
            @Override
            public void widgetSelected(SelectionEvent event) {
                shell.close();
            }
        });

    }

    public String getBoldMessage() {
        return boldMessage;
    }

    public void setBoldMessage(String message) {
        boldMessage = message;
    }

    public String getLinkText() {
        return linkText;
    }

    public void setLinkText(String linkText) {
        this.linkText = linkText;
    }
}