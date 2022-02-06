package project.demo.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Entity

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class CoinStock {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    Long id;
    String stockname;
    Integer amount;

}
