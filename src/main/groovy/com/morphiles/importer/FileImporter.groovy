package com.morphiles.importer

import org.springframework.stereotype.Component

@Component
public interface FileImporter {
  public void importFile(File file);
}
