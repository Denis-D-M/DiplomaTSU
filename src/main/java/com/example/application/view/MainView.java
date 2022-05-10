package com.example.application.view;

import static org.junit.platform.engine.discovery.DiscoverySelectors.selectPackage;

import com.vaadin.flow.component.html.Div;
import com.vaadin.flow.component.html.H1;
import com.vaadin.flow.component.html.H2;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.progressbar.ProgressBar;
import com.vaadin.flow.component.progressbar.ProgressBarVariant;
import com.vaadin.flow.component.upload.Upload;
import com.vaadin.flow.component.upload.receivers.MultiFileMemoryBuffer;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;
import java.io.File;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.util.Scanner;
import org.junit.platform.launcher.Launcher;
import org.junit.platform.launcher.LauncherDiscoveryRequest;
import org.junit.platform.launcher.LauncherSession;
import org.junit.platform.launcher.TestPlan;
import org.junit.platform.launcher.core.LauncherDiscoveryRequestBuilder;
import org.junit.platform.launcher.core.LauncherFactory;
import org.junit.platform.launcher.listeners.SummaryGeneratingListener;
import org.junit.platform.launcher.listeners.TestExecutionSummary;

@PageTitle("Тест Java")
@Route(value = "")
public class MainView extends VerticalLayout {

  public MainView() {
    setSpacing(false);

    MultiFileMemoryBuffer multiFileMemoryBuffer = new MultiFileMemoryBuffer();
    Upload multiUpload = new Upload(multiFileMemoryBuffer);

    multiUpload.addSucceededListener(event -> {
      File root = new File("src/main/java/com/example/application/test");

      File sourceFile = new File(root,
          parsePackage(multiFileMemoryBuffer.getInputStream(event.getFileName())) + "/" + event.getFileName());

      sourceFile.getParentFile().mkdirs();
      try {
        Files.write(sourceFile.toPath(),
            multiFileMemoryBuffer.getOutputBuffer(event.getFileName()).toString().getBytes(StandardCharsets.UTF_8));

        TestExecutionSummary testExecutionSummary = runAll();

        ProgressBar barFound = new ProgressBar();
        barFound.setWidth("500px");
        barFound.setMin(0);
        barFound.setMax(testExecutionSummary.getTestsFoundCount());
        barFound.setValue(testExecutionSummary.getTestsFoundCount());
        barFound.addThemeVariants(ProgressBarVariant.LUMO_CONTRAST);

        Div found = new Div();
        found.setText("Tests found: " + testExecutionSummary.getTestsFoundCount());
        found.add(barFound);

        ProgressBar barSuccess = new ProgressBar();
        barSuccess.setWidth("500px");
        barSuccess.setMin(0);
        barSuccess.setMax(testExecutionSummary.getTestsFoundCount());
        barSuccess.setValue(testExecutionSummary.getTestsSucceededCount());
        barSuccess.addThemeVariants(ProgressBarVariant.LUMO_SUCCESS);

        Div successful = new Div();
        successful.setText("Tests successful: " + testExecutionSummary.getTestsSucceededCount());
        successful.add(barSuccess);

        ProgressBar barFailed = new ProgressBar();
        barFailed.setWidth("500px");
        barFailed.setMin(0);
        barFailed.setMax(testExecutionSummary.getTestsFoundCount());
        barFailed.setValue(testExecutionSummary.getTestsFailedCount());
        barFailed.addThemeVariants(ProgressBarVariant.LUMO_ERROR);

        Div failed = new Div();
        failed.setText("Tests failed: " + testExecutionSummary.getTestsFailedCount());
        failed.add(barFailed);

        add(new H2());
        add(new H1(
            "Speed: " + (testExecutionSummary.getTimeFinished() - testExecutionSummary.getTimeStarted()) + "ms."));
        add(found);
        add(successful);
        add(failed);

      } catch (Exception e) {
        e.printStackTrace();
      }
    });

    add(new H2("Test your java code."));
    add(multiUpload);

    setSizeFull();
    setJustifyContentMode(JustifyContentMode.CENTER);
    setDefaultHorizontalComponentAlignment(Alignment.CENTER);
    getStyle().set("text-align", "center");
  }

  private String parsePackage(InputStream source) {
    Scanner scanner = new Scanner(source);
    scanner.next();
    return scanner.next().replaceAll("\\.", "\\/").replaceAll(";", "");

  }

  public static TestExecutionSummary runAll() {
    SummaryGeneratingListener listener = new SummaryGeneratingListener();

    LauncherDiscoveryRequest request = LauncherDiscoveryRequestBuilder.request()
        .selectors(
            selectPackage("com.example.application.test")
        )
        .build();
    try (LauncherSession session = LauncherFactory.openSession()) {
      Launcher launcher = session.getLauncher();
      launcher.registerTestExecutionListeners(listener);
      TestPlan discover = launcher.discover(request);
      launcher.execute(discover);
      launcher.execute(request);
    }
    return listener.getSummary();

  }

}
