package de.projectdw.springfxui.data;

import lombok.Data;

import java.util.Date;
import java.util.List;

@Data
public class LogData {
    Date date = new Date();
    boolean hasInternetConnection;
    List<String> networkInterfaceNames;
}
