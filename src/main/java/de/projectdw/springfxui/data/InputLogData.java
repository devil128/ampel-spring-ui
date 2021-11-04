package de.projectdw.springfxui.data;

import lombok.Data;

import java.util.List;

@Data
public class InputLogData {
    String name;
    String place;
    String timestamp;
    List<NetworkPair> networkInterfaces;
}
