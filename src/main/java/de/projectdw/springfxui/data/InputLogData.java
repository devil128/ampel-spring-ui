package de.projectdw.springfxui.data;

import lombok.Data;

import java.io.Serializable;
import java.util.List;

@Data
/**
 * Data class to send information of current or past state
 */
public class InputLogData {
    String name;
    String place;
    String timestamp;
    List<NetworkPair> networkInterfaces;
}
