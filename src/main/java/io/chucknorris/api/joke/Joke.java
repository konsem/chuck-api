package io.chucknorris.api.joke;

import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.vladmihalcea.hibernate.type.json.JsonBinaryType;
import java.io.Serializable;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Transient;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.Type;
import org.hibernate.annotations.TypeDef;
import org.hibernate.annotations.TypeDefs;

@AllArgsConstructor
@Builder(toBuilder = true)
@Data
@Entity
@JsonSerialize(using = JokeSerializer.class)
@NoArgsConstructor
@Table(name = "joke")
@TypeDefs({
    @TypeDef(name = "jsonb", typeClass = JsonBinaryType.class)
})
public class Joke implements Serializable {

  @Column(name = "categories", columnDefinition = "jsonb")
  @Type(type = "jsonb")
  private String[] categories;

  @Column(name = "created_at")
  private String createdAt;

  @Transient
  final private String iconUrl = "https://assets.chucknorris.host/img/avatar/chuck-norris.png";

  @Id
  @Column(name = "joke_id", updatable = false, nullable = false)
  private String id;

  @Column(name = "updated_at")
  private String updatedAt;

  @Transient
  private String url;

  @Column(name = "value")
  private String value;
}
