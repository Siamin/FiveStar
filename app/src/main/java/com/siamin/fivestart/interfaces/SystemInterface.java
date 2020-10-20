package com.siamin.fivestart.interfaces;

import com.siamin.fivestart.models.SystemModel;

public interface SystemInterface {
    void setNewSystem(SystemModel model);
    void removeSystem(int index);
    void editSystem(SystemModel model,int index);

}
